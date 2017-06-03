package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException

/** Performs a "svn cleanup" on a working-copy directory */
class SvnCleanup extends SvnBaseTask {

  @Internal final cleanup = []

  /** To specify directories to be cleaned up */
  void setCleanup(target) {
    cleanup.clear()
    cleanup << target
  }

  void cleanup(Object... targets) {
    targets.each {
      cleanup << it
    }
  }

  @TaskAction
  def run() {
    def wcClient = SvnSupport.createSvnClientManager(username, password, proxy).WCClient
    project.files(cleanup).each { file ->
      if (file.isDirectory()) {
        try {
          wcClient.doCleanup(file)
        } catch (SVNException e) {
          throw new InvalidUserDataException("svn-cleanup failed for $file.absolutePath\n" + e.message, e)
        }
      } else {
        throw new InvalidUserDataException("Not a directory: $file")
      }
    }
  }
}
