package at.bxm.gradleplugins.svntools.internal

import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNNodeKind
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.wc.SVNCopySource
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNWCUtil

abstract class SvnCopy extends SvnBaseTask {

  /** Local workspace that should be copied (default: {@code project.projectDir}) */
  def workspaceDir
  /** An optional commit message. */
  String commitMessage
  /** If {@code true}, the target will be removed first if it already exists */
  boolean replaceExisting

  abstract String getDestinationPath()

  @TaskAction
  def run() {
    def clientManager = createSvnClientManager()
    def workspace = workspaceDir ? project.file(workspaceDir, PathValidation.DIRECTORY) : project.projectDir
    def info
    try {
      info = clientManager.WCClient.doInfo(workspace, SVNRevision.WORKING)
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-info failed for $workspace.absolutePath\n" + e.message, e)
    }
    def copySource = new SVNCopySource(info.revision, info.revision, info.URL)
    def basePath = SvnPath.parse(info.URL).moduleBasePath
    def fullDestPath = "$basePath/$destinationPath"
    try {
      if (replaceExisting) {
        def repo = remoteRepository(info.URL.setPath(basePath, false))
        if (existsInRepo(repo, destinationPath)) {
          deleteFromRepo(repo, destinationPath)
        }
      }
      def destUrl = info.URL.setPath(fullDestPath, false)
      logger.info "Copying $info.URL at revision $info.revision to $destUrl"
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

  private SVNRepository remoteRepository(SVNURL repositoryUrl) {
    def repo = SVNRepositoryFactory.create(repositoryUrl)
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
    svnPath =~ '^[a-zA-Z0-9_\\-.]+$'
  }
}
