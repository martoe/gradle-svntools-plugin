package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException

import static at.bxm.gradleplugins.svntools.internal.SvnSupport.revisionFrom

class SvnUpdate extends SvnBaseTask {

  /** Local workspace that should be updated (default: {@code project.projectDir}) */
  def workspaceDir
  /** The target revision number (optional, defaults to HEAD)  */
  Long revision

  @TaskAction
  def run() {
    def rev = revisionFrom(revision)
    def dir = workspaceDir ? project.file(workspaceDir, PathValidation.DIRECTORY) : project.projectDir
    try {
      def targetRev = createSvnClientManager().updateClient.doUpdate(dir, rev, SVNDepth.INFINITY, false, false)
      if (targetRev < 0) {
        throw new InvalidUserDataException("workspaceDir $dir.absolutePath is no SVN workspace")
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-update failed for $dir.absolutePath\n" + e.message, e)
    }
  }
}
