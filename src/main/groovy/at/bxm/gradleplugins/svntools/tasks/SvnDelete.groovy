package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth

/** Provides add / delete operations on WC Files */
class SvnDelete extends SvnBaseTask {

  /** Continue the build if the specified paths conflict with the WC status (can't delete) (default: {@code false}) */
  boolean ignoreErrors = false

  final delete = []

  String depth

  /** To specify files to be scheduled for deletion */
  void setDelete(target) {
    delete.clear()
    delete << target
  }

  void delete(Object... targets) {
    targets.each {
      delete << it
    }
  }

  @TaskAction
  def run() {
    SVNDepth svnDepth = (depth ? SVNDepth.fromString(depth) : SVNDepth.FILES)
    project.files(delete).each { file ->
      SvnSupport.doSvnDelete(file, getUsername(), getPassword(), proxy, ignoreErrors)
    }
  }
}
