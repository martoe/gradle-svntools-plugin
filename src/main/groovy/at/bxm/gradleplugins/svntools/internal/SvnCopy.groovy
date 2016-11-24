package at.bxm.gradleplugins.svntools.internal

import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNCopySource
import org.tmatesoft.svn.core.wc.SVNInfo
import org.tmatesoft.svn.core.wc.SVNRevision

abstract class SvnCopy extends SvnBaseTask {

  /** The remote SVN URL to be copied from. Optional - fallback to {@link #workspaceDir} if missing. */
  @Internal String svnUrl
  /** Local workspace that should be copied (default: {@code project.projectDir}) */
  @Internal workspaceDir
  /** An optional commit message. */
  @Internal String commitMessage
  /** If {@code true}, the target will be removed first if it already exists */
  @Internal boolean replaceExisting
  /** If local changes should be committed to the copy target. Only used it not svnUrl is provided. */
  @Internal boolean localChanges
  /**
   * Set to {@code true} if the target name contains arbitrary chars (as supported by the current SVN server and operation system).
   * If {@code false} (default), only a reduced subset of chars (a-z, A-Z, 0-9, "_", "-", ".", and "/") is allowed
   */
  @Internal boolean specialChars

  @Internal
  abstract String getDestinationPath()

  @TaskAction
  def run() {
    def clientManager = createSvnClientManager()
    def sourceInfo = svnUrl ? fromRemote() : fromWorkspace(clientManager)
    def copySource = sourceInfo.copySource
    def sourceUrl = sourceInfo.url
    def basePath = SvnPath.parse(sourceUrl).moduleBasePath
    def fullDestPath = "$basePath/$destinationPath"
    try {
      if (replaceExisting) {
        def repo = SvnSupport.remoteRepository(sourceUrl.setPath(basePath, false), username, password, proxy)
        if (existsInRepo(repo, destinationPath)) {
          deleteFromRepo(repo, destinationPath)
        }
      }
      def destUrl = sourceUrl.setPath(fullDestPath, false)
      logger.info "Copying $sourceUrl at revision $copySource.revision to $destUrl"
      def copied = clientManager.copyClient.doCopy([copySource] as SVNCopySource[], destUrl, false, true, true, commitMessage, null);
      if (copied.errorMessage) {
        if (copied.errorMessage.warning) {
          logger.warn "svn-copy completed with warning: $copied"
        } else {
          throw new InvalidUserDataException("svn-copy failed: $copied")
        }
      } else {
        logger.info "svn-copy successful: $copied"
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-copy failed for $fullDestPath\n$e.message", e)
    }
  }

  private def fromWorkspace(SVNClientManager clientManager) {
    def workspace = workspaceDir ? project.file(workspaceDir, PathValidation.DIRECTORY) : project.projectDir
    try {
      SVNInfo info = clientManager.WCClient.doInfo(workspace, SVNRevision.WORKING)
      if (localChanges) {
        return [copySource: new SVNCopySource(SVNRevision.WORKING, SVNRevision.WORKING, workspace), url: info.URL]
      } else {
        return [copySource: new SVNCopySource(info.revision, info.revision, info.URL), url: info.URL]
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-info failed for $workspace.absolutePath\n" + e.message, e)
    }
  }

  private def fromRemote() {
    if (workspaceDir) {
      throw new InvalidUserDataException("Either 'svnUrl' or 'workspaceDir' may be set")
    }
    try {
      SVNURL url = SVNURL.parseURIEncoded(svnUrl)
      return [copySource: new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, url), url: url]
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $svnUrl", e)
    }
  }

  private static boolean existsInRepo(SVNRepository repo, String path) {
    return repo.checkPath(path, -1) != SVNNodeKind.NONE
  }

  private static void deleteFromRepo(SVNRepository repo, String path) {
    def editor = repo.getCommitEditor("creating a new file", null)
    editor.openRoot(-1)
    editor.deleteEntry(path, -1)
    editor.closeEdit()
  }

  boolean isValidName(String svnPath) {
    specialChars || svnPath =~ '^[a-zA-Z0-9_\\-./]+$'
  }
}
