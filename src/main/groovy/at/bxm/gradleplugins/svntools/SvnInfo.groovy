package at.bxm.gradleplugins.svntools

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.wc.SVNRevision

class SvnInfo extends SvnBaseTask {

  /** Source directory for reading the SVN metadata (defaults to {@link Project#getProjectDir()}) */
  def sourceDir
  boolean ignoreErrors

  @TaskAction
  def run() {
    def result = new SvnData()
    project.ext { svnData = result }
    try {
      def srcDir = sourceDir != null ? project.file(sourceDir, PathValidation.DIRECTORY) : project.projectDir
      def info = createSvnClientManager().WCClient.doInfo srcDir, SVNRevision.WORKING
      result.revision = info.revision
      result.url = info.URL
      result.repositoryRootUrl = info.repositoryRootURL
      def path = info.URL.path
      if (path.endsWith("/trunk")) {
        result.trunk = "trunk"
        logger.info "Working copy is on trunk at revision $result.revision"
      } else {
        def matcher = path =~ '^.+/branches/(.+)$'
        if (matcher) {
          result.branch = matcher[0][1]
          logger.info "Working copy is on branch $result.branch at revision $result.revision"
        } else {
          matcher = path =~ '^.+/tags/(.+)$'
          if (matcher) {
            result.tag = matcher[0][1]
            logger.info "Working copy is on tag $result.tag at revision $result.revision"
          } else {
            logger.warn "SVN path has an unexpected layout: $path"
          }
        }
      }
    } catch (Exception e) {
      if (ignoreErrors) {
        logger.warn "Could not execute svn-info on $sourceDir: $e"
      } else {
        throw new InvalidUserDataException("Could not execute svn-info on $sourceDir", e)
      }
    }
  }
}
