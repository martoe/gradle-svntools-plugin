package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnCopy
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal

/** Creates an SVN tag based on a local SVN workspace. */
class SvnTag extends SvnCopy {

  /** Name of the new SVN tag (required, no default) */
  @Internal String tagName

  @Override
  String getDestinationPath() {
    if (!tagName) {
      throw new InvalidUserDataException("tagName missing")
    }
    if (!isValidName(tagName)) {
      throw new InvalidUserDataException("tagName contains invalid chars: $tagName")
    }
    return "tags/$tagName"
  }
}
