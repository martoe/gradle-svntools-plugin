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
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

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
    def e = thrown TaskExecutionException
    e.cause.message =~ "svn-checkout failed for .*"
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
    def e = thrown TaskExecutionException
    e.cause.message =~ ".* must be an empty directory"
  }

  def "no target dir"() {
    when: "running the SvnCheckout task without target dir"
    task.svnUrl = localRepoUrl
    task.execute()

    then:
    def e = thrown TaskExecutionException
    e.cause.message == "targetDir missing"
  }
}
