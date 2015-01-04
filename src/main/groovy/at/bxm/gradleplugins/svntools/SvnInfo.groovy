package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.wc.SVNRevision

/** Provides information about the current SVN workspace */
class SvnInfo extends SvnBaseTask {

  /** Source path for reading the SVN metadata (default: {@code project.projectDir}) */
  def sourcePath
  /** The name of the project extra property that will receive the resulting {@link SvnData} object (default: {@code svnData}) */
  String targetPropertyName
  /** Continue the build if the specified path doesn't contain SVN data (default: {@code false}) */
  boolean ignoreErrors

  @TaskAction
  def run() {
    def result = new SvnData()
    project.ext.set(targetPropertyName ?: "svnData", result)
    def srcPath = sourcePath != null ? project.file(sourcePath, PathValidation.EXISTS) : project.projectDir
    try {
      def info = createSvnClientManager().WCClient.doInfo srcPath, SVNRevision.WORKING
      result.revisionNumber = info.revision.number
      result.url = info.URL
      result.repositoryRootUrl = info.repositoryRootURL
      try {
        def svnPath = SvnPath.parse info.URL
        if (svnPath.trunk) {
          result.trunk = "trunk"
          logger.info "Working copy is on trunk at revision $result.revisionNumber"
        } else if (svnPath.branch) {
          result.branch = svnPath.branchName
          logger.info "Working copy is on branch $result.branch at revision $result.revisionNumber"
        } else if (svnPath.tag) {
          result.tag = svnPath.tagName
          logger.info "Working copy is on tag $result.tag at revision $result.revisionNumber"
        }
      } catch (MalformedURLException e) {
        logger.warn "SVN path has an unexpected layout: $e.message"
      }
    } catch (Exception e) {
      if (ignoreErrors) {
        logger.warn "Could not execute svn-info on $srcPath.absolutePath ($e.message)"
      } else {
        throw new InvalidUserDataException("Could not execute svn-info on $srcPath.absolutePath ($e.message)", e)
      }
    }
  }
}
