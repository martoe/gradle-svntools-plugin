package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction

/** Provides information about the current SVN workspace */
class SvnInfo extends SvnBaseTask {

  /** Source path for reading the SVN metadata (default: {@code project.projectDir}) */
  def sourcePath
  /** The name of the project extra property that will receive the resulting {@link at.bxm.gradleplugins.svntools.api.SvnData} object (default: {@code svnData}) */
  String targetPropertyName
  /** Continue the build if the specified path doesn't contain SVN data (default: {@code false}) */
  boolean ignoreErrors

  @TaskAction
  def run() {
    def srcPath = sourcePath != null ? project.file(sourcePath, PathValidation.EXISTS) : project.projectDir
    def result = SvnSupport.createSvnData(srcPath, getUsername(), getPassword(), ignoreErrors)
    project.ext.set(targetPropertyName ?: "svnData", result)
  }
}
