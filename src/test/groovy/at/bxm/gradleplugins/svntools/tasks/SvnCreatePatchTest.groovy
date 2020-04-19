package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.InvalidUserDataException

class SvnCreatePatchTest extends SvnWorkspaceTestSupport {

  def "single file"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"

    when: "running the SvnCreatePatch task"
    def task = taskWithType(SvnCreatePatch)
    task.source(file)
    task.patchFile = "myPatch.txt"
    task.run()

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
    def task = taskWithType(SvnCreatePatch)
    task.source = file
    task.patchFile = "myPatch.txt"
    task.run()

    then: "useful error message"
    def exception = thrown InvalidUserDataException
    exception.message.readLines().size() == 2
    exception.message.readLines()[1] == "Invalid source file or directory"
  }

  def "patchfile already exists"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"
    def patchFile = newFile("myPatch.txt")

    when: "running the SvnCreatePatch task"
    def task = taskWithType(SvnCreatePatch)
    task.source(file)
    task.patchFile = patchFile
    task.run()

    then: "patchfile is overwritten"
    patchFile.text.readLines().size() == 7
  }
}
