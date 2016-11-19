package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException

/** Schedules files (within an SVN working copy) to be removed from version control (and deleted locally) */
class SvnDelete extends SvnBaseTask {

  @Internal final delete = []
  /** Continue the build if the specified paths conflict with the WC status (can't delete) (default: {@code false}) */
  @Internal boolean ignoreErrors

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
    def wcClient = SvnSupport.createSvnClientManager(username, password, proxy).WCClient
    project.files(delete).each { file ->
      try {
        wcClient.doDelete(file, ignoreErrors, false)
      } catch (SVNException e) {
        if (ignoreErrors) {
          logger.warn "Could not execute svn-delete on $file.absolutePath ($e.message)"
        } else {
          throw new InvalidUserDataException("Could not execute svn-delete on $file.absolutePath ($e.message)", e)
        }
      }
    }
  }
}
