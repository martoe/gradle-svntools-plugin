package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.wc.SVNRevision

import static at.bxm.gradleplugins.svntools.internal.SvnSupport.*

class SvnExport extends SvnBaseTask {

  /** The remote SVN URL to be exported */
  String svnUrl
  /** The target directory for export (required). If it doesn't exist it will be created. If it exists it must be empty. */
  def targetDir
  /** The revision number to be exported (optional, defaults to HEAD)  */
  Long revision

  @TaskAction
  def run() {
    def rev = revisionFrom(revision)
    def repoUrl
    try {
      repoUrl = SVNURL.parseURIEncoded(svnUrl)
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $svnUrl", e)
    }
    if (!targetDir) {
      throw new InvalidUserDataException("targetDir must be specified")
    }
    def dir = targetDir instanceof File ? targetDir : targetDir.toString() as File
    if (dir.exists()) {
      if (!dir.isDirectory()) {
        throw new InvalidUserDataException("targetDir $dir.absolutePath must be a directory")
      }
      if (dir.list()) {
        throw new InvalidUserDataException("targetDir $dir.absolutePath must be an empty directory")
      }
    }
    try {
      createSvnClientManager().updateClient.doExport(repoUrl, dir, SVNRevision.UNDEFINED, rev, null, true, SVNDepth.INFINITY)
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-export failed for $svnUrl\n" + e.message, e)
    }
  }
}
