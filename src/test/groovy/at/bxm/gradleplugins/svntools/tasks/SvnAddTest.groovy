package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.Project

class SvnAddTest extends SvnTestSupport {

  File workspace
  Project project
  SvnAdd task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnAdd, "add") as SvnAdd
  }

  def "add a new file"() {
    given:
    def newFile = newFile("newfile.txt")

    when: "running the SvnAdd task"
    task.add(newFile)
    task.execute()

    then: "workspace dirty"
    SvnSupport.createSvnVersionData(workspace, null, null, null, false) as String == "1M"
    // TODO more precise assertion
  }
  
  // TODO write further tests (error conditions)
  
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
