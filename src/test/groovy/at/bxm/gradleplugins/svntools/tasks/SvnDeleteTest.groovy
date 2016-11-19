package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.Project

class SvnDeleteTest extends SvnTestSupport {

  File workspace
  Project project
  SvnDelete task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnDelete, "delete") as SvnDelete
  }

  def "deleta a file"() {
    given:
    def existingFile = existingFile("test.txt")

    when: "running the SvnDelete task"
    task.delete(existingFile)
    task.execute()

    then: "workspace dirty"
    SvnSupport.createSvnVersionData(workspace, null, null, null, false) as String == "1M"
    // TODO more precise assertion
  }
  
  // TODO write further tests (error conditions)

  private File existingFile(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    return file
  }
}
