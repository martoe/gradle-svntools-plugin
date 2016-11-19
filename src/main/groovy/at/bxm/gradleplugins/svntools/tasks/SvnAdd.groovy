package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth

/** Provides add / delete operations on WC Files */
class SvnAdd extends SvnBaseTask {

  /** Continue the build if the specified paths conflict with the WC status (can't add) (default: {@code false}) */
  boolean ignoreErrors = false

  final add = []

  /** To specify files to be scheduled for addition */
  void setAdd(target) {
    add.clear()
    add << target
  }

  void add(Object... targets) {
    targets.each {
      add << it
    }
  }

  String depth

  @TaskAction
  def run() {
    SVNDepth svnDepth = (depth ? SVNDepth.fromString(depth) : SVNDepth.FILES)
    File[] files = project.files(add).getFiles().toArray()
    SvnSupport.doSvnAdd(files, svnDepth, getUsername(), getPassword(), proxy, ignoreErrors)
  }
}
