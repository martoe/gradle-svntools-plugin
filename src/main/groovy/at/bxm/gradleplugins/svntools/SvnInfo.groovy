package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.wc.SVNRevision

/** Provides information about the current SVN workspace */
class SvnInfo extends SvnBaseTask {

  /** Source directory for reading the SVN metadata (default: {@code project.projectDir}) */
  def sourcePath // TODO support filenames here as well
  /** the name of the project extra property that will receive the resulting {@link SvnData} object (default: {@code svnData}) */
  String targetPropertyName
  /** Continue the build if the specified path doesn't contain SVN data (default: {@code false}) */
  boolean ignoreErrors

  @TaskAction
  def run() {
    def result = new SvnData()
    project.ext.set(targetPropertyName ?: "svnData", result)
    try {
      def srcDir = sourcePath != null ? project.file(sourcePath, PathValidation.DIRECTORY) : project.projectDir
      def info = createSvnClientManager().WCClient.doInfo srcDir, SVNRevision.WORKING
      result.revisionNumber = info.revision.number
      result.url = info.URL
      result.repositoryRootUrl = info.repositoryRootURL
      def path = info.URL.path
      if (path.endsWith("/trunk")) {
        result.trunk = "trunk"
        logger.info "Working copy is on trunk at revision $result.revisionNumber"
      } else {
        def matcher = path =~ '^.+/branches/(.+)$'
        if (matcher) {
          result.branch = matcher[0][1]
          logger.info "Working copy is on branch $result.branch at revision $result.revisionNumber"
        } else {
          matcher = path =~ '^.+/tags/(.+)$'
          if (matcher) {
            result.tag = matcher[0][1]
            logger.info "Working copy is on tag $result.tag at revision $result.revisionNumber"
          } else {
            logger.warn "SVN path has an unexpected layout: $path"
          }
        }
      }
    } catch (Exception e) {
      if (ignoreErrors) {
        logger.warn "Could not execute svn-info on $sourcePath: $e"
      } else {
        throw new InvalidUserDataException("Could not execute svn-info on $sourcePath", e)
      }
    }
  }
}
