package at.bxm.gradleplugins.svntools

import org.gradle.api.InvalidUserDataException

/** Creates an SVN tag based on a local SVN workspace. */
class SvnTag extends SvnCopy {

  /** Name of the new SVN tag (required, no default) */
  String tagName

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
