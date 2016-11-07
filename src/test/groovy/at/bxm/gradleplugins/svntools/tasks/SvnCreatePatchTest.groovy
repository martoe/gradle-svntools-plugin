package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

class SvnCreatePatchTest extends SvnTestSupport {

  File workspace
  Project project
  SvnCreatePatch task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnCreatePatch, "createPatch") as SvnCreatePatch
  }

  def "single file"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"

    when: "running the SvnCreatePatch task"
    task.source(file)
    task.patchFile = "myPatch.txt"
    task.execute()

    then: "patchfile contains one added line"
    def patchFile = project.file(task.patchFile)
    patchFile.exists() == true
    patchFile.text.readLines().size() == 7
    patchFile.text.readLines()[4] == "@@ -0,0 +1 @@"
    patchFile.text.readLines()[5] == "+changed"
  }

  def "file outside a workspace"() {
    given:
    def file = new File(tempDir, "file-outside-workspace.txt")
    file.text = "blah"

    when: "running the SvnCreatePatch task"
    task.source(file)
    task.patchFile = "myPatch.txt"
    task.execute()

    then: "useful error message"
    def exception = thrown TaskExecutionException
    exception.cause.message.readLines().size() == 2
    exception.cause.message.readLines()[1] == "Invalid source file or directory"
  }

  private File existingFile(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    return file
  }
}
