package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project

class SvnRevertTest extends SvnTestSupport {

  File workspace
  Project project
  SvnRevert task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnRevert, "revert") as SvnRevert
  }

  def "reverting a single file"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"

    when: "running the SvnRevert task"
    task.revert << file
    task.execute()

    then: "file reverted"
    file.text == ""
  }

  def "reverting a directory non-recursively"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"

    when: "running the SvnRevert task on the base directory only"
    task.revert << workspace
    task.execute()

    then: "file not reverted"
    file.text == "changed"
  }


  def "reverting a directory recursively"() {
    given:
    def file = existingFile("test.txt")
    file.text = "changed"

    when: "running the SvnRevert task on the base directory recursively"
    task.revert << workspace
    task.recursive = true
    task.execute()

    then: "file reverted"
    file.text == ""
  }

  private File existingFile(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    return file
  }
}
