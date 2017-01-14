package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.wc.SVNInfo
import org.tmatesoft.svn.core.wc.SVNRevision

/** Commits a list of files (and directories) within the current SVN workspace. */
class SvnCommit extends SvnBaseTask {

  /**
   * A list of files and directories that should be committed.
   * If these are not under version control already, they will be added first.
   * If this list is empty of the files contain no modifications, no commit will be executed.
   */
  @Internal source = []
  /** Also commit items in subdirectories (default: {@code false}) */
  @Internal boolean recursive
  /** An optional commit message. */
  @Internal String commitMessage

  @TaskAction
  run() {
    if (!source) {
      logger.warn "No files to commit"
      return
    }
    def addedFiles = []
    def clientManager = createSvnClientManager()
    SVNDepth svnDepth = recursive ? SVNDepth.INFINITY : SVNDepth.EMPTY
    source.each {
      def file = project.file(it)
      SVNInfo status
      try {
        status = clientManager.WCClient.doInfo(file, SVNRevision.WORKING)
      } catch (SVNException e) {
        status = null // not under version control yet
      }
      if (status?.schedule == "delete") {
        logger.debug("File {} is already scheduled for delete", file.absolutePath)
      } else if (!file.exists()) {
        throw new InvalidUserDataException("File " + file.absolutePath + " doesn't exist")
      } else if (status?.schedule == "add") {
        logger.debug("File {} is already scheduled for add", file.absolutePath)
      } else { // status?.schedule == null
        logger.debug("Adding {} to SVN", file.absolutePath)
        try {
          clientManager.WCClient.doAdd(file, true, false, false, svnDepth, false, false)
        } catch (SVNException e) {
          throw new InvalidUserDataException("svn-add failed for $file.absolutePath\n" + e.message, e)
        }
      }
      addedFiles << file
    }
    logger.info "Committing $addedFiles"
    try {
      def committed = clientManager.commitClient.doCommit(addedFiles as File[], false, commitMessage, null, null, false, true, svnDepth)
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
