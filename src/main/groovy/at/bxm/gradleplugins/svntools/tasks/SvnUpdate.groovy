package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException

import static at.bxm.gradleplugins.svntools.internal.SvnSupport.*

class SvnUpdate extends SvnBaseTask {

  /** Local workspace that should be updated (default: {@code project.projectDir}) */
  @Internal workspaceDir
  /** The target revision number (optional, defaults to HEAD)  */
  @Internal Long revision

  @TaskAction
  run() {
    def rev = revisionFrom(revision)
    def dir = workspaceDir ? project.file(workspaceDir, PathValidation.DIRECTORY) : project.projectDir
    try {
      def targetRev = createSvnClientManager().updateClient.doUpdate(dir, rev, SVNDepth.INFINITY, false, false)
      if (targetRev < 0) {
        // this has been working for svnkit-1.8.12
        // svnkit-1.8.15 throws an exception instead ("E155007: None of the targets are working copies")
        throw new InvalidUserDataException("workspaceDir $dir.absolutePath is no SVN workspace")
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-update failed for $dir.absolutePath\n" + e.message, e)
    }
  }
}
