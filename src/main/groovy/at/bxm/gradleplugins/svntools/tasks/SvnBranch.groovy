package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnCopy
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal

/** Creates an SVN branch based on a local SVN workspace. */
class SvnBranch extends SvnCopy {

  /** Name of the new SVN branch (required, no default) */
  @Internal String branchName

  @Override
  String getDestinationPath() {
    if (!branchName) {
      throw new InvalidUserDataException("branchName missing")
    }
    if (!isValidName(branchName)) {
      throw new InvalidUserDataException("branchName contains invalid chars: $branchName")
    }
    return "branches/$branchName"
  }
}
