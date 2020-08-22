package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.InvalidUserDataException

class SvnExportTest extends SvnTestSupport {

  File workspace
  Project project
  SvnExport task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnExport, "export") as SvnExport
  }

  def "happy path"() {
    given: "nonexistent targetDir"
    def targetDir = new File("$tempDir.absolutePath/myTargetDir")
    assert !targetDir.exists()

    when: "running the SvnExport task"
    task.svnUrl = localRepoUrl.appendPath("trunk", false)
    task.targetDir = targetDir
    task.run()

    then: "targetDir exists"
    targetDir.exists()
    targetDir.list().sort() as List == ["dir", "test.txt"]
  }

  def "invalid remote URL"() {
    when: "running the SvnExport task"
    task.svnUrl = "$localRepoUrl/blah"
    task.targetDir = new File(tempDir, "targetDir")
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ "svn-export failed for .*"
  }

  def "non-empty targetDir"() {
    given: "a non-empty targetDir"
    def targetDir = new File(tempDir, "myTargetDir")
    targetDir.mkdirs()
    new File(targetDir, "someFile.txt").text = "placeholder"

    when: "running the SvnExport task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.targetDir = targetDir
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ ".* must be an empty directory"
  }
}
