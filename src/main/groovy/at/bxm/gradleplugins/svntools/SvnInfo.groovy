package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNURL
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
      if (isTrunk(info.URL)) {
        result.trunk = "trunk"
        logger.info "Working copy is on trunk at revision $result.revisionNumber"
      } else {
        result.branch = getBranchName(info.URL)
        if (result.branch) {
          logger.info "Working copy is on branch $result.branch at revision $result.revisionNumber"
        } else {
          result.tag = getTagName(info.URL)
          if (result.tag) {
            logger.info "Working copy is on tag $result.tag at revision $result.revisionNumber"
          } else {
            logger.warn "SVN path has an unexpected layout: $info.URL.path"
          }
        }
      }
    } catch (Exception e) {
      if (ignoreErrors) {
        logger.warn "Could not execute svn-info on $srcPath.absolutePath ($e.message)"
      } else {
        throw new InvalidUserDataException("Could not execute svn-info on $srcPath.absolutePath ($e.message)", e)
      }
    }
  }

  private static boolean isTrunk(SVNURL url) {
    url.path.endsWith("/trunk") || url.path =~ '^.+/trunk/(.+)$'
  }

  private static String getBranchName(SVNURL url) {
    def matcher = url.path =~ '^.+/branches/(.+)$'
    if (matcher) {
      def branchPath = matcher[0][1] as String
      def endOfBranchName = branchPath.indexOf "/"
      return endOfBranchName > 0 ? branchPath.substring(0, endOfBranchName) : branchPath
    }
    return null
  }

  private static String getTagName(SVNURL url) {
    def matcher = url.path =~ '^.+/tags/(.+)$'
    if (matcher) {
      def tagPath = matcher[0][1] as String
      def endOfTagName = tagPath.indexOf "/"
      return endOfTagName > 0 ? tagPath.substring(0, endOfTagName) : tagPath
    }
    return null
  }
}
