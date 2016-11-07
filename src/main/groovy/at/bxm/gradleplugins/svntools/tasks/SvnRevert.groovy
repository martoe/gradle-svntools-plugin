package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException

import static org.tmatesoft.svn.core.SVNDepth.*

/**
 * Reverts all local changes of the given files or directories (which must be part of an SVN working copy).
 * Items that are not under version control will be ignored.
 * 
 * Future improvements:
 *  - optionally delete unversionized files
 */
class SvnRevert extends SvnBaseTask {

  /** Local workspace files or directories that should be reverted (default: {@code project.projectDir}) */
  final revert = []
  /** Also revert items in subdirectories (default: {@code false}) */
  boolean recursive

  void setRevert(target) {
    revert.clear()
    revert << target
  }

  void revert(Object... targets) {
    targets.each {
      revert << it
    }
  }

  FileCollection getTargetFiles() {
    return project.files(revert ? revert : project.projectDir);
  }

  @TaskAction
  def run() {
    def depth = recursive ? INFINITY : EMPTY
    def targets = targetFiles.files
    logger.debug("Reverting with depth {}: {}", depth, targets)
    try {
      createSvnClientManager().WCClient.doRevert(targets as File[], depth, null)
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-revert failed for $targets\n" + e.message, e)
    }
  }
}
