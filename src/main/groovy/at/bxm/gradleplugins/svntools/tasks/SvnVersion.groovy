package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.api.SvnData
import at.bxm.gradleplugins.svntools.api.SvnVersionData
import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.wc.ISVNStatusHandler
import org.tmatesoft.svn.core.wc.SVNRevision
import org.tmatesoft.svn.core.wc.SVNStatus
import org.tmatesoft.svn.core.wc.SVNStatusType

/** Provides information similar to the <a href="http://svnbook.red-bean.com/en/1.7/svn.ref.svnversion.re.html">"svnversion"</a> command */
class SvnVersion extends SvnBaseTask {

  /** Local SVN working copy directory (default: {@code project.projectDir}) */
  def sourcePath
  /** The name of the project extra property that will receive the resulting {@link SvnVersionData} object (default: {@code svnVersion}) */
  String targetPropertyName

  @TaskAction
  def run() {
    def srcPath = sourcePath != null ? project.file(sourcePath, PathValidation.EXISTS) : project.projectDir
    def versionHandler = new VersionHandler()
    SvnSupport.createSvnClientManager(username, password, proxy).statusClient.doStatus(
            srcPath, SVNRevision.UNDEFINED, SVNDepth.INFINITY, false, true, false, false, versionHandler, null)
    project.ext.set(targetPropertyName ?: "svnVersion", versionHandler.version)
  }

  class VersionHandler implements ISVNStatusHandler {
    final version = new SvnVersionData()

    @Override
    void handleStatus(SVNStatus status) {
      version.minRevisionNumber = version.minRevisionNumber == SvnData.UNKNOWN_REVISION ? status.revision.number : Math.min(version.minRevisionNumber, status.revision.number)
      version.maxRevisionNumber = version.maxRevisionNumber == SvnData.UNKNOWN_REVISION ? status.revision.number : Math.max(version.maxRevisionNumber, status.revision.number)
      if (status.contentsStatus != SVNStatusType.STATUS_NORMAL) {
        // TODO use "combinedNodeAndContentsStatus" instead?
        logger.info("$status.repositoryRelativePath has status $status.contentsStatus - workspace is dirty")
        version.modified = true
      }
      // TODO also check "propertiesStatus"?
      version.switched |= status.switched
    }
  }
}
