package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException

/** Schedules files (within an SVN working copy) to be added to version control */
class SvnAdd extends SvnBaseTask {

  /** Local files or directories (within a workspace) that should be added */
  @Internal final add = []
  /** Also add items in subdirectories (default: {@code false}) */
  @Internal boolean recursive
  /** Continue the build if the specified paths conflict with the WC status (can't add) (default: {@code false}) */
  @Internal boolean ignoreErrors

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

  @TaskAction
  def run() {
    SVNDepth svnDepth = recursive ? SVNDepth.INFINITY : SVNDepth.EMPTY
    File[] files = project.files(add).getFiles().toArray()
    if (!files) {
      if (ignoreErrors) {
        logger.warn("No files found for adding")
      } else {
        throw new InvalidUserDataException("No files to add specified")
      }
    } else {
      try {
        SvnSupport.createSvnClientManager(username, password, proxy).WCClient.doAdd(
          files, ignoreErrors, false, true, svnDepth, false, false, true)
      } catch (SVNException e) {
        if (ignoreErrors) {
          logger.warn "Could not execute svn-add on ${files*.absolutePath} ($e.message)"
        } else {
          throw new InvalidUserDataException("Could not execute svn-add on ${files*.absolutePath} ($e.message)", e)
        }
      }
    }
  }
}
