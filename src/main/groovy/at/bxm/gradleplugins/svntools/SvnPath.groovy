package at.bxm.gradleplugins.svntools

import groovy.transform.ToString
import org.tmatesoft.svn.core.SVNURL

/** Split an SVN URL into its semantic parts (trunk, branch, or tag) */
@ToString(includePackage = false, includeNames = true, ignoreNulls = true)
class SvnPath {

  final String moduleBasePath
  final String branchName
  final String tagName
  final String path

  private SvnPath(String moduleBasePath, String branchName, String tagName, String path) {
    this.moduleBasePath = moduleBasePath
    this.branchName = branchName
    this.tagName = tagName
    this.path = path
  }

  boolean isTrunk() {
    !branchName && !tagName
  }

  boolean isBranch() {
    branchName
  }

  boolean isTag() {
    tagName
  }

  boolean isRootPath() {
    path == "/"
  }

  static SvnPath parse(SVNURL url) throws MalformedURLException {
    if (url.path.endsWith("/trunk")) {
      return new SvnPath(url.path[0..-7], null, null, "/")
    }
    def matcher = url.path =~ '^(.+)/trunk(/.+)$'
    if (matcher) {
      return new SvnPath(matcher[0][1] as String, null, null, matcher[0][2] as String)
    }
    matcher = url.path =~ '^(.+)/branches/(.+)$'
    if (matcher) {
      def branchPath = matcher[0][2] as String
      def endOfBranchName = branchPath.indexOf "/"
      if (endOfBranchName > 0) {
        return new SvnPath(matcher[0][1] as String, branchPath[0..endOfBranchName - 1], null, branchPath[endOfBranchName..-1])
      } else {
        return new SvnPath(matcher[0][1] as String, branchPath, null, "/")
      }
    }
    matcher = url.path =~ '^(.+)/tags/(.+)$'
    if (matcher) {
      def tagPath = matcher[0][2] as String
      def endOfTagName = tagPath.indexOf "/"
      if (endOfTagName > 0) {
        return new SvnPath(matcher[0][1] as String, null, tagPath[0..endOfTagName - 1], tagPath[endOfTagName..-1])
      } else {
        return new SvnPath(matcher[0][1] as String, null, tagPath, "/")
      }
    }
    throw new MalformedURLException(url.path)
  }
}
