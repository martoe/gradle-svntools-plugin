package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.tmatesoft.svn.core.SVNException

import static org.tmatesoft.svn.core.SVNDepth.*
import static org.tmatesoft.svn.core.wc.SVNRevision.*

/**
 * Creates a patch file that contains all modifications of local workspace files and directories (including subdirectories).
 *
 * Corresponds to a `svn diff > filename.patch`
 */
class SvnCreatePatch extends SvnBaseTask {

  /** Local workspace files and directories with modifications that shall be saved to a patch file (default: {@code project.projectDir}) */
  @Internal final source = []
  /** The name of the target patch file (required). If it exists it will be overwritten */
  @Internal patchFile

  void setSource(path) {
    source.clear()
    source << path
  }

  void source(Object... paths) {
    paths.each {
      source << it
    }
  }

  private FileCollection sourceFiles() {
    return project.files(source ? source : project.projectDir);
  }

  @TaskAction
  def run() {
    def sources = sourceFiles().files
    def targetFile = project.file(patchFile)
    if (targetFile.exists()) {
      if (targetFile.file) {
        if (!targetFile.delete()) {
          throw new InvalidUserDataException("Could not delete $targetFile.absolutePath")
        }
      } else {
        throw new InvalidUserDataException("$targetFile.absolutePath is a directory")
      }
    }
    logger.debug("Writing diffs of {} to {}", sources, targetFile.absolutePath)
    try {
      targetFile.withOutputStream {
        createSvnClientManager().diffClient.doDiff(sources as File[], BASE, WORKING, UNDEFINED, INFINITY, true, it, null)
      }
    } catch (SVNException e) {
      final msg
      if (e.errorMessage?.errorCode?.code == 200007) {
        // misleading error message "svn: E200007: Runner for 'org.tmatesoft.svn.core.wc2.SvnDiff' command have not been found; probably not yet implement in this API"
        msg = "Invalid source file or directory"
      } else {
        msg = e.message
      }
      throw new InvalidUserDataException("svn-patch failed for $targetFile.absolutePath\n$msg", e)
    }
  }
}
