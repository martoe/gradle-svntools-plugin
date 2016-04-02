package at.bxm.gradleplugins.svntools.api

/** Output of the {@link at.bxm.gradleplugins.svntools.tasks.SvnVersion} task */
class SvnVersionData {

  long minRevisionNumber = SvnData.UNKNOWN_REVISION
  long maxRevisionNumber = SvnData.UNKNOWN_REVISION
  // TODO modifications (M), switched (S), sparse (P)?

  boolean isMixedRevision() {
    return minRevisionNumber != maxRevisionNumber
  }

  @Override
  String toString() {
    return mixedRevision ? "$minRevisionNumber:$maxRevisionNumber" : "$minRevisionNumber"
  }
}
