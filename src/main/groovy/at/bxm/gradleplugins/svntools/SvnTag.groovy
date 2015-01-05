package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.wc.*

/** Creates an SVN tag based on a local SVN workspace. */
class SvnTag extends SvnBaseTask {

  /** Local workspace that should be tagged (default: {@code project.projectDir}) */
  def workspaceDir
  /** Name of the SVN tag (required, no default) */
  String tagName
  /** An optional commit message. */
  String commitMessage

  @TaskAction
  def run() {
    if (!tagName) {
      throw new InvalidUserDataException("tagName missing")
    }
    if (!(tagName =~ '^[a-zA-Z0-9_\\-.]+$')) {
      throw new InvalidUserDataException("tagName contains invalid chars: $tagName")
    }
    def clientManager = createSvnClientManager()
    def info
    def workspace = workspaceDir ? project.file(workspaceDir, PathValidation.DIRECTORY) : project.projectDir
    try {
      info = clientManager.WCClient.doInfo(workspace, SVNRevision.WORKING)
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-info failed for $workspace.absolutePath\n" + e.message, e)
    }
    def basePath = SvnPath.parse(info.URL).moduleBasePath
    def copySource = new SVNCopySource(info.revision, info.revision, info.URL)
    def tagPath = "$basePath/tags/$tagName"
    try {
      def tagUrl = info.URL.setPath(tagPath, false)
      logger.info "Tagging $info.URL at revision $info.revision as $tagUrl"
      def tagged = clientManager.copyClient.doCopy([copySource] as SVNCopySource[], tagUrl, false, false, true, commitMessage, null);
      if (tagged.errorMessage) {
        if (tagged.errorMessage.warning) {
          logger.warn "svn-copy completed with warning: $tagged"
        } else {
          throw new InvalidUserDataException("svn-copy failed: $tagged")
        }
      } else {
        logger.info "svn-copy successful: $tagged"
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-copy failed for $tagPath\n$e.message", e)
    }
  }
}
