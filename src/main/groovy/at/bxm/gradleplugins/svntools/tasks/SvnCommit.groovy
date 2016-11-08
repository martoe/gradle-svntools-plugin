package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.PathValidation
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException

/** Commits a list of files (and directories) within the current SVN workspace. */
class SvnCommit extends SvnBaseTask {

  /**
   * A list of files and directories that should be committed.
   * If these are not under version control already, they will be added first.
   * If this list is empty of the files contain no modifications, no commit will be executed.
   */
  @Internal source = []
  /** An optional commit message. */
  @Internal String commitMessage

  @TaskAction
  def run() {
    if (!source) {
      logger.warn "No files to commit"
      return
    }
    def addedFiles = []
    def clientManager = createSvnClientManager()
    source.each {
      def file = project.file(it, PathValidation.EXISTS)
      // don't know if this file is already in SVN, so to be sure we add it with "force=true":
      try {
        clientManager.WCClient.doAdd(file, true, false, false, SVNDepth.EMPTY, false, false)
        addedFiles << file
      } catch (SVNException e) {
        throw new InvalidUserDataException("svn-add failed for $file.absolutePath\n" + e.message, e)
      }
    }
    logger.info "Committing $addedFiles"
    try {
      def committed = clientManager.commitClient.doCommit(addedFiles as File[], false, commitMessage, null, null, false, true, SVNDepth.EMPTY)
      if (committed.errorMessage) {
        if (committed.errorMessage.warning) {
          logger.warn "Commit completed with warning: $committed"
        } else {
          throw new InvalidUserDataException("svn-commit failed: $committed")
        }
      } else {
        logger.info "svn-commit successful: $committed"
      }
    } catch (SVNException e) {
      throw new InvalidUserDataException("svn-commit failed for $addedFiles\n" + e.message, e)
    }
  }
}
