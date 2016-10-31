package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import at.bxm.gradleplugins.svntools.internal.SvnSupport

import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNException


/** Provides add / delete operations on WC Files */
class SvnWCFiles extends SvnBaseTask {

  /** Source path for the WC (default: {@code project.projectDir}) */
  def sourcePath = project.projectDir
  /** Continue the build if the specified paths conflict with the WC status (can't dadd/delete) (default: {@code false}) */
  boolean ignoreErrors = false

  FileTree filesToAdd =null
  FileTree filesToDelete =null

  /** To specify files to be scheduled for addition */
  void add(Closure patternSet) {
    def newFileTree = project.fileTree(sourcePath, patternSet)
    if (!filesToAdd) {
      filesToAdd = newFileTree
    } else {
      filesToAdd += newFileTree
    }
  }

  String depth

  /** To specify files to be scheduled for deletion */
  void delete(Closure patternSet) {
    def newFileTree = project.fileTree(sourcePath, patternSet)
    if (!filesToDelete) {
      filesToDelete = newFileTree
    } else {
      filesToDelete += newFileTree
    }
  }

  @TaskAction
  def run() {
    if (filesToDelete) {
      SVNDepth svnDepth = (depth ? SVNDepth.fromString(depth) : SVNDepth.Files)
      filesToDelete.files.each {file ->
        SvnSupport.doSvnDelete(file, getUsername(), getPassword(), proxy, ignoreErrors)
      }
    }
    if (filesToAdd) {
      SVNDepth svnDepth = (depth ? SVNDepth.fromString(depth) : SVNDepth.FILES)
      if (! filesToAdd.isEmpty()) {
		File[] files = filesToAdd.getFiles().toArray()
        SvnSupport.doSvnAdd(files, svnDepth, getUsername(), getPassword(), proxy, ignoreErrors)
      }
    }
  }
}
