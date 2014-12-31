package at.bxm.gradleplugins.svntools

import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

class SvnCommitTest extends SvnTestSupport {

  // commit without changes
  // commit a new file inside a new directory

  File workspace
  Project project

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
  }

  def "commit a new file"() {
    given:
    def newFile = newFile("newfile.txt")

    when: "running the SvnCommit task"
    def task = project.tasks["svnCommit"] as SvnCommit
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
    def task = project.tasks["svnCommit"] as SvnCommit
    task.source << file
    task.execute()

    then: "file committed, new revision"
    getRevision(file) == 2
  }

  def "empty commit"() {
    when: "running the SvnCommit task"
    def task = project.tasks["svnCommit"] as SvnCommit
    task.execute()

    then: "no new revision"
    getRevision(workspace) == 1
  }

  def "commit without changes"() {
    given:
    def file = existingFile("test.txt")

    when: "running the SvnCommit task"
    def task = project.tasks["svnCommit"] as SvnCommit
    task.source << file
    task.execute()

    then: "no new revision"
    getRevision(file) == 1
  }

  def "commit a new file inside an unversioned directory"() {
    given:
    def newFile = newFile("newfile.txt", new File(workspace, "newdir"))

    when: "running the SvnCommit task"
    def task = project.tasks["svnCommit"] as SvnCommit
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
    def task = project.tasks["svnCommit"] as SvnCommit
    task.source << newDir << newFile
    task.execute()

    then: "file committed, new revision"
    getRevision(newFile) == 2
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

  private long getRevision(File file) {
    def task = project.tasks["svnInfo"] as SvnInfo
    task.sourcePath = file
    task.execute()
    return project.ext.svnData.revisionNumber
  }
}
