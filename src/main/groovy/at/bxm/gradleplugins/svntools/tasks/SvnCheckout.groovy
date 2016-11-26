package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.api.SvnDepth
import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNRevision

import static at.bxm.gradleplugins.svntools.internal.SvnSupport.*

class SvnCheckout extends SvnBaseTask {

  /** The remote SVN URL to be checked out */
  @Internal String svnUrl
  /** The target directory for checkout (required). If it doesn't exist it will be created. If it exists it must be empty. */
  @Internal workspaceDir
  /** The revision number to be checked out (optional, defaults to HEAD)  */
  @Internal Long revision
  /**
   * The checkout depth (optional, defaults to INFINITY)
   * @see SvnDepth
   */
  @Internal depth
  /** If {@code true}, an "svn update" is performed if the {@link #workspaceDir} already contains checked-out data. */
  @Internal update

  @TaskAction
  def run() {
    def rev = revisionFrom(revision)
    def repoUrl
    try {
      repoUrl = SVNURL.parseURIEncoded(svnUrl)
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $svnUrl", e)
    }
    if (!workspaceDir) {
      throw new InvalidUserDataException("workspaceDir must be specified")
    }
    def dir = workspaceDir instanceof File ? workspaceDir : workspaceDir.toString() as File
    def performUpdate = false
    if (dir.exists()) {
      if (!dir.isDirectory()) {
        throw new InvalidUserDataException("workspaceDir $dir.absolutePath must be a directory")
      }
      if (dir.list()) {
        if (!update) {
          throw new InvalidUserDataException("workspaceDir $dir.absolutePath must be an empty directory")
        }
        def result = createSvnData(dir, getUsername(), getPassword(), proxy, true)
        if (!result.url) {
          throw new InvalidUserDataException("workspaceDir $dir.absolutePath must be either an empty directory or an SVN workspace")
        }
        if (result.url != repoUrl.toString()) {
          throw new InvalidUserDataException("SVN location of $dir.absolutePath is invalid: $result.url")
        }
        performUpdate = true
      }
    }
    try {
      if (performUpdate) {
        createSvnClientManager().updateClient.doUpdate(dir, rev, parseDepth(depth), false, false)
      } else {
        createSvnClientManager().updateClient.doCheckout(repoUrl, dir, SVNRevision.UNDEFINED, rev, parseDepth(depth), false)
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException((performUpdate ? "svn-update" : "svn-checkout") + " failed for $svnUrl\n" + e.message, e)
    }
  }

  private static SVNDepth parseDepth(depth) {
    if (depth) {
      switch (depth.toString().toLowerCase()) {
        case "empty": return SVNDepth.EMPTY
        case "files": return SVNDepth.FILES
        case "immediates": return SVNDepth.IMMEDIATES
        case "infinity": return SVNDepth.INFINITY
        default:
          throw new InvalidUserDataException("Invalid depth value: $depth")
      }
    }
    return SVNDepth.INFINITY
  }
}
