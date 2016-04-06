package at.bxm.gradleplugins.svntools.api

/** Output of the {@link at.bxm.gradleplugins.svntools.tasks.SvnVersion} task */
class SvnVersionData {

  long minRevisionNumber = SvnData.UNKNOWN_REVISION
  long maxRevisionNumber = SvnData.UNKNOWN_REVISION
  /** "true" if the working copy contains at least one file with modified content */
  boolean modified
  /** "true" if parts of the working copy have been switched */
  boolean switched
  /** "true" if the working copy is sparsely populated (i.e. "depth" is not "infinity") */
  boolean sparse

  boolean isMixedRevision() {
    return minRevisionNumber != maxRevisionNumber
  }

  @Override
  String toString() {
    if (minRevisionNumber == SvnData.UNKNOWN_REVISION) {
      return "exported" // see bottom of http://svnbook.red-bean.com/en/1.6/svn.ref.svnversion.re.html
    }
    def result = mixedRevision ? "$minRevisionNumber:$maxRevisionNumber" : "$minRevisionNumber"
    if (modified) {
      result += "M"
    }
    if (switched) {
      result += "S"
    }
    if (sparse) {
      result += "P"
    }
    return result
  }
}
