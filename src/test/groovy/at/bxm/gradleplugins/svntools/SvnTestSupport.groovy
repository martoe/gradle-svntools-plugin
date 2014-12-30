package at.bxm.gradleplugins.svntools

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*
import org.tmatesoft.svn.core.*
import org.tmatesoft.svn.core.io.*
import org.tmatesoft.svn.core.wc.*

trait SvnTestSupport {

  private File tempDir
  private SVNClientManager clientManager
  private SVNURL localRepoUrl

  Project projectWithPlugin() {
    def project = ProjectBuilder.builder().build()
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

  private File checkoutLocalRepo(String path) {
    def workspaceDir = new File(tempDir, "workspace")
    clientManager.updateClient.doCheckout(localRepoUrl.appendPath(path, false), workspaceDir, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false)
    return workspaceDir
  }

  @Before
  void initTempDir() {
    tempDir = new File(System.getProperty("java.io.tmpdir") + "/svntest-" + System.currentTimeMillis())
    clientManager = SVNClientManager.newInstance()
    if (!tempDir.mkdir()) {
      fail "Could not create directory $tempDir.absolutePath"
    }
  }

  @After
  void deleteTempDir() {
    if (!tempDir.deleteDir()) {
      fail "Could not delete directory $tempDir.absolutePath"
    }
  }
}
