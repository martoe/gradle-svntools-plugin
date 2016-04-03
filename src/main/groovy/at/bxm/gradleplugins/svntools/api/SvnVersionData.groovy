package at.bxm.gradleplugins.svntools.api

/** Output of the {@link at.bxm.gradleplugins.svntools.tasks.SvnVersion} task */
class SvnVersionData {

  long minRevisionNumber = SvnData.UNKNOWN_REVISION
  long maxRevisionNumber = SvnData.UNKNOWN_REVISION
  /** "true" if the workspace contains at least one file with modified content */
  boolean modified
  /** "true" if parts of the workspace have been switched */
  boolean switched
  // TODO implement sparse population (P) - http://svnbook.red-bean.com/en/1.7/svn.advanced.sparsedirs.html

  boolean isMixedRevision() {
    return minRevisionNumber != maxRevisionNumber
  }

  @Override
  String toString() {
    def result = mixedRevision ? "$minRevisionNumber:$maxRevisionNumber" : "$minRevisionNumber"
    if (modified) {
      result += "M"
    }
    if (switched) {
      result += "S"
    }
    return result
  }
}
