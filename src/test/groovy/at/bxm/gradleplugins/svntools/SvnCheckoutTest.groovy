package at.bxm.gradleplugins.svntools

import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

class SvnCheckoutTest extends SvnTestSupport {

  File workspace
  Project project
  SvnCheckout task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnCheckout, "checkout")
  }

  def "happy path"() {
    given: "nonexistent workspace"
    def workspaceDir = new File(tempDir, "myNewWorkspace")
    assert !workspaceDir.exists()

    when: "running the SvnCheckout task"
    task.svnUrl = localRepoUrl
    task.targetDir = workspaceDir
    task.execute()

    then: "local workspace exists"
    getRevision(workspaceDir) == 1
  }

  def "invalid remote URL"() {
    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/blah"
    task.targetDir = new File(tempDir, "workspace")
    task.execute()

    then:
    thrown TaskExecutionException
  }

  def "checkout using a non-empty dir"() {
    given: "a non-empty workspace"
    def workspaceDir = new File(tempDir, "myNewWorkspace")
    workspaceDir.mkdirs()
    new File(workspaceDir, "test.txt").text = "placeholder"

    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.targetDir = workspaceDir
    task.execute()

    then:
    thrown TaskExecutionException
  }
}
