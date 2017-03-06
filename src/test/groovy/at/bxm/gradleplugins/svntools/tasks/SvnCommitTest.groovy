package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException
import org.tmatesoft.svn.core.SVNDepth

class SvnCommitTest extends SvnTestSupport {

  File workspace
  Project project
  SvnCommit task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnCommit, "commit") as SvnCommit
  }

  def "commit a new file"() {
    given:
    def newFile = newFile("newfile.txt")

    when: "running the SvnCommit task"
    task.source << newFile
    task.execute()

    then: "file committed, new revision"
    getRevision(newFile) == 2
  }

  def "commit an added file"() {
    given:
    def newFile = newFile("newfile.txt")
    clientManager.WCClient.doAdd(newFile, false, false, false, SVNDepth.INFINITY, false, false)

    when: "running the SvnCommit task"
    task.source << newFile
    task.execute()

    then: "file committed, new revision"
    getRevision(newFile) == 2
  }

  def "commit an existing file"() {
    given:
    def file = existingFile("test.txt")
    file.text = "new file"

    when: "running the SvnCommit task"
    task.source << file
    task.execute()

    then: "file committed, new revision"
    getRevision(file) == 2
  }

  def "empty commit"() {
    when: "running the SvnCommit task"
    task.execute()

    then: "no new revision"
    getRevision(workspace) == 1
  }

  def "commit without changes"() {
    given:
    def file = existingFile("test.txt")

    when: "running the SvnCommit task"
    task.source << file
    task.execute()

    then: "no new revision"
    getRevision(file) == 1
  }

  def "commit a new file inside an unversioned directory"() {
    given:
    def newFile = newFile("newfile.txt", new File(workspace, "newdir"))

    when: "running the SvnCommit task"
    task.source << newFile
    task.execute()

    then:
    thrown TaskExecutionException
  }

  def "commit a new file inside a new directory"() {
    given:
    def newDir = new File(workspace, "newdir")
    def newFile = newFile("newfile.txt", newDir)

    when: "running the SvnCommit task"
    task.source << newDir << newFile
    task.execute()

    then: "file committed, new revision"
    getRevision(newFile) == 2
  }

  def "commit a new directory recursively"() {
    given:
    def newDir = new File(workspace, "newdir")
    def newFile = newFile("newfile.txt", newDir)

    when: "running the SvnCommit task"
    task.source << newDir
    task.recursive = true
    task.execute()

    then: "directory and file committed, new revision"
    getRevision(newDir) == 2
    getRevision(newFile) == 2
  }

  def "commit a changed file in a subdirectory"() {
    given: "changed file in a subdirectory"
    def unchangedDir = new File(workspace, "dir")
    def changedFile = new File(unchangedDir, "test.txt")
    changedFile.text = "changed"

    when: "running the SvnCommit task on the directory"
    task.source = [unchangedDir]
    task.recursive = true
    task.execute()

    then: "file committed, new revision"
    getRevision(unchangedDir) == 1
    getRevision(changedFile) == 2
  }

  def "commit a file outside of the workspace"() {
    given:
    def newValidFile = newFile("newfile.txt")
    def newInvalidFile = newFile("newfile.txt", "$workspace.absolutePath/.." as File)

    when: "running the SvnCommit task"
    task.source << newValidFile
    task.source << newInvalidFile
    task.execute()

    then: "file committed, new revision"
    def e = thrown TaskExecutionException
    e.cause.message =~ "svn-add failed for .*"
  }

  def "commit a deleted directory"(boolean deleteLocally) {
    given: "directory marked for deletion"
    def deleteDir = new File(workspace, "dir")
    clientManager.WCClient.doDelete(deleteDir, false, deleteLocally, false)

    when: "running the SvnCommit task"
    task.source << deleteDir
    task.execute()

    then: "dir deleted, new revision"
    workspace.deleteDir()
    checkoutTrunk()
    deleteDir.exists() == false
    getRevision(workspace) == 2

    where:
    deleteLocally | _
    true          | _
    false         | _
  }

  private File newFile(String name, File path = workspace) {
    def file = new File(path, name)
    assert !file.exists(), "$file.absolutePath already exist"
    path.mkdirs()
    file.text = "new file"
    return file
  }

  private File existingFile(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    return file
  }
}
