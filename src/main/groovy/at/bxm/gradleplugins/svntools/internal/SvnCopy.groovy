package at.bxm.gradleplugins.svntools.internal

import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNCopySource
import org.tmatesoft.svn.core.wc.SVNInfo
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNWCUtil

abstract class SvnCopy extends SvnBaseTask {

  /** The remote SVN URL to be copied from. Optional - fallback to {@link #workspaceDir} if missing. */
  String svnUrl
  /** Local workspace that should be copied (default: {@code project.projectDir}) */
  def workspaceDir
  /** An optional commit message. */
  String commitMessage
  /** If {@code true}, the target will be removed first if it already exists */
  boolean replaceExisting

  /** If Local changes should be commited to the copy target. Only used it not svnUrl is provided. */
  boolean localChanges

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
        def repo = remoteRepository(sourceUrl.setPath(basePath, false))
        if (existsInRepo(repo, destinationPath)) {
          deleteFromRepo(repo, destinationPath)
        }
      }
      def destUrl = sourceUrl.setPath(fullDestPath, false)
      logger.info "Copying $sourceUrl at revision $copySource.revision to $destUrl"
      def copied = clientManager.copyClient.doCopy([copySource] as SVNCopySource[], destUrl, false, false, true, commitMessage, null);
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
      if(localChanges) {
        logger.error("FILE BASED")
        return [ copySource: new SVNCopySource(SVNRevision.WORKING, SVNRevision.WORKING, workspace), url: info.URL ]
      } else {
        logger.error("REVISION BASED")
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
      return [ copySource: new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, url), url: url ]
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $svnUrl", e)
    }
  }

  private SVNRepository remoteRepository(SVNURL repositoryUrl) {
    def repo = SVNRepositoryFactory.create(repositoryUrl)
    logger.error("REMOTE BASED")
    repo.authenticationManager = SVNWCUtil.createDefaultAuthenticationManager(username, password)
    return repo
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

  static boolean isValidName(String svnPath) {
    svnPath =~ '^[a-zA-Z0-9_\\-./]+$'
  }
}
