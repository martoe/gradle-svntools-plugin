package at.bxm.gradleplugins.svntools.api

import groovy.transform.ToString

/** Provides information about a SVN workspace */
@ToString(includePackage = false, includeNames = true, ignoreNulls = true)
class SvnData {
  static final long UNKNOWN_REVISION = -1
  long revisionNumber = UNKNOWN_REVISION
  java.util.Date committedDate
  String committedAuthor
  /** The complete SVN URL of the checked-out project */
  String url
  /** The root URL of the SVN repository */
  String repositoryRootUrl
  /**
   * Either "trunk", the name of the branch (i.e. the path segment succeeding the "branches" segment), or the name of
   * the tag (i.e. the path segment succeeding the "tags" segment)
   */
  String name
  /** If the SVN URL refers to a trunk (i.e. it contains a "trunk" path segment) */
  boolean trunk
  /** If the SVN URL refers to a branch (i.e. it contains a "branches" path segment) */
  boolean branch
  /** If the SVN URL refers to a tag (i.e. it contains a "tags" path segment) */
  boolean tag
}
