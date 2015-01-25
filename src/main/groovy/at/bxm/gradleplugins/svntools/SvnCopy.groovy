package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.wc.*

abstract class SvnCopy extends SvnBaseTask {

  /** Local workspace that should be copied (default: {@code project.projectDir}) */
  def workspaceDir
  /** An optional commit message. */
  String commitMessage

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

  static boolean isValidName(String svnPath) {
    svnPath =~ '^[a-zA-Z0-9_\\-.]+$'
  }
}
