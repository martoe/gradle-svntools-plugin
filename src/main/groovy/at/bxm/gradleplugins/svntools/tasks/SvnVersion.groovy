package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.api.SvnData
import at.bxm.gradleplugins.svntools.api.SvnVersionData
import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.PathValidation
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.wc.ISVNInfoHandler
import org.tmatesoft.svn.core.wc.SVNInfo
import org.tmatesoft.svn.core.wc.SVNRevision

/** Provides information similar to the <a href="http://svnbook.red-bean.com/en/1.7/svn.ref.svnversion.re.html">"svnversion"</a> command */
class SvnVersion extends SvnBaseTask {

  /** Source path for reading the SVN metadata (default: {@code project.projectDir}) */
  def sourcePath
  /** The name of the project extra property that will receive the resulting {@link SvnVersionData} object (default: {@code svnVersion}) */
  String targetPropertyName

  @TaskAction
  def run() {
    def srcPath = sourcePath != null ? project.file(sourcePath, PathValidation.EXISTS) : project.projectDir
    def version = new SvnVersionData()
    SvnSupport.createSvnClientManager(username, password, proxy).WCClient.doInfo(
            srcPath, SVNRevision.UNDEFINED, SVNRevision.WORKING, SVNDepth.INFINITY, null, new ISVNInfoHandler() {
      @Override
      void handleInfo(SVNInfo info) {
        version.minRevisionNumber = version.minRevisionNumber == SvnData.UNKNOWN_REVISION ? info.revision.number : Math.min(version.minRevisionNumber, info.revision.number)
        version.maxRevisionNumber = version.maxRevisionNumber == SvnData.UNKNOWN_REVISION ? info.revision.number : Math.max(version.maxRevisionNumber, info.revision.number)
      }
    })
    project.ext.set(targetPropertyName ?: "svnVersion", version)
  }
}
