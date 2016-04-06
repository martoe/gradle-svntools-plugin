package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.api.SvnVersionData
import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction

/** Provides information similar to the <a href="http://svnbook.red-bean.com/en/1.7/svn.ref.svnversion.re.html">"svnversion"</a> command */
class SvnVersion extends SvnBaseTask {

  /** Local SVN working copy directory (default: {@code project.projectDir}) */
  def sourcePath
  /** The name of the project extra property that will receive the resulting {@link SvnVersionData} object (default: {@code svnVersion}) */
  String targetPropertyName
  /** Continue the build if the specified path is no SVN working copy */
  boolean ignoreErrors

  @TaskAction
  def run() {
    def srcPath = sourcePath != null ? project.file(sourcePath, PathValidation.EXISTS) : project.projectDir
    def result = SvnSupport.createSvnVersionData(srcPath, getUsername(), getPassword(), proxy, ignoreErrors)
    project.ext.set(targetPropertyName ?: "svnVersion", result)
  }
}
