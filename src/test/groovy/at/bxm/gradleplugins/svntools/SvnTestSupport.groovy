package at.bxm.gradleplugins.svntools

import groovy.util.logging.Log
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.io.ISVNEditor
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.wc.SVNClientManager
import org.tmatesoft.svn.core.wc.SVNRevision
import spock.lang.Specification

@Log
abstract class SvnTestSupport extends Specification {

  File tempDir
  private SVNClientManager clientManager
  SVNURL localRepoUrl

  Project projectWithPlugin(File projectDir = null) {
    def projectBuilder = ProjectBuilder.builder()
    if (projectDir) {
      projectBuilder.withProjectDir(projectDir)
    }
    def project = projectBuilder.build()
    project.apply plugin: "at.bxm.svntools"
    return project
  }

  SVNURL createLocalRepo() {
    def localRepoDir = new File(tempDir, "repo")
    localRepoUrl = SVNRepositoryFactory.createLocalRepository(localRepoDir, true, false)
    def repo = SVNRepositoryFactory.create(localRepoUrl)
    def editor = repo.getCommitEditor("creating a new file", null)
    editor.openRoot(-1)
    editor.addDir("trunk", null, -1)
    addEmptyFile(editor, "trunk/test.txt")
    editor.addDir("branches", null, -1)
    editor.addDir("branches/test-branch", null, -1)
    addEmptyFile(editor, "branches/test-branch/test.txt")
    editor.addDir("tags", null, -1)
    editor.addDir("tags/test-tag", null, -1)
    addEmptyFile(editor, "tags/test-tag/test.txt")
    editor.closeDir()
    editor.closeEdit()
    return localRepoUrl
  }

  void addFile(String path) {
    def repo = SVNRepositoryFactory.create(localRepoUrl)
    def editor = repo.getCommitEditor("creating a new file", null)
    editor.openRoot(-1)
    addEmptyFile(editor, path)
    editor.closeDir()
    editor.closeEdit()
  }

  private static ISVNEditor addEmptyFile(ISVNEditor editor, String path) {
    editor.addFile path, null, -1
    editor.applyTextDelta path, null
    editor.textDeltaEnd path
    editor.closeFile path, null
    editor
  }

  File checkoutTrunk() {
    return checkoutLocalRepo("trunk")
  }

  File checkoutBranch() {
    return checkoutLocalRepo("branches/test-branch")
  }

  File checkoutTag() {
    return checkoutLocalRepo("tags/test-tag")
  }

  File checkoutLocalRepo(String path) {
    def workspaceDir = new File(tempDir, "workspace")
    clientManager.updateClient.doCheckout(localRepoUrl.appendPath(path, false), workspaceDir, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false)
    return workspaceDir
  }

  void switchLocalRepo(String path) {
    clientManager.updateClient.doSwitch(new File(tempDir, "workspace"), localRepoUrl.appendPath(path, false), SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false, false)
  }

  void updateLocalRepo() {
    clientManager.updateClient.doUpdate(new File(tempDir, "workspace"), SVNRevision.HEAD, SVNDepth.INFINITY, false, false)
  }

  long getRevision(file) {
    clientManager.WCClient.doInfo(file as File, SVNRevision.WORKING).revision.number
  }

  def setup() {
    tempDir = new File(System.getProperty("java.io.tmpdir") + "/svntest-" + System.currentTimeMillis())
    clientManager = SVNClientManager.newInstance()
    assert tempDir.mkdir(), "Could not create directory $tempDir.absolutePath"
  }

  def cleanup() {
    if (!tempDir.deleteDir()) {
      log.warning("Could not delete directory $tempDir.absolutePath")
    }
  }
}
