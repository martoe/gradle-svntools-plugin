package at.bxm.gradleplugins.svntools

import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.*
import org.tmatesoft.svn.core.wc.SVNRevision

class SvnCheckout extends SvnBaseTask {

  /** The remote SVN URL to be checked out */
  String svnUrl
  /** The target directory for checkout (required). If it doesn't exist it will be created. If it exists it must be empty. */
  File targetDir
  /** The revision number to be checked out (optional, defaults to HEAD)  */
  Long revision

  @TaskAction
  def run() {
    def rev = revision ? SVNRevision.create(revision) : SVNRevision.HEAD
    def repoUrl
    try {
      repoUrl = SVNURL.parseURIEncoded(svnUrl)
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $svnUrl", e)
    }
    if (targetDir.exists() && !(targetDir.isDirectory() && !targetDir.list())) {
      throw new InvalidUserDataException("$targetDir.absolutePath must be an empty directory")
    }
    try {
      createSvnClientManager().updateClient.doCheckout(repoUrl, targetDir, SVNRevision.UNDEFINED, rev, SVNDepth.INFINITY, false)
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-checkout failed for $svnUrl\n" + e.message, e)
    }
  }
}
