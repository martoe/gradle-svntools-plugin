package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNRevision

class SvnCheckout extends SvnBaseTask {

  /** The remote SVN URL to be checked out */
  String svnUrl
  /** The target directory for checkout (required). If it doesn't exist it will be created. If it exists it must be empty. */
  def workspaceDir
  /** The revision number to be checked out (optional, defaults to HEAD)  */
  Long revision
  /** If {@code true}, an "svn update" is performed if the {@link #workspaceDir} already contains checked-out data. */
  boolean update

  @TaskAction
  def run() {
    def rev = revision ? SVNRevision.create(revision) : SVNRevision.HEAD
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
        def result = SvnSupport.createSvnData(dir, getUsername(), getPassword(), proxy, true)
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
        createSvnClientManager().updateClient.doUpdate(dir, rev, SVNDepth.INFINITY, false, false)
      } else {
        createSvnClientManager().updateClient.doCheckout(repoUrl, dir, SVNRevision.UNDEFINED, rev, SVNDepth.INFINITY, false)
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException((performUpdate ? "svn-update" : "svn-checkout") + " failed for $svnUrl\n" + e.message, e)
    }
  }
}
