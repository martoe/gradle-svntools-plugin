package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

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

  def "commit a file outside of the workspace"() {
    given:
    def newValidFile = newFile("newfile.txt")
    def newInvalidFile = newFile("newfile.txt","$workspace.absolutePath/.." as File)

    when: "running the SvnCommit task"
    task.source << newValidFile
    task.source << newInvalidFile
    task.execute()

    then: "file committed, new revision"
    def e = thrown TaskExecutionException
    e.cause.message =~ "svn-add failed for .*"
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
