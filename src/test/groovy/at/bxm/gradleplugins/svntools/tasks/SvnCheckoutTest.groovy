package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.SvnToolsPluginExtension
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

class SvnCheckoutTest extends SvnTestSupport {

  File workspace
  Project project
  SvnCheckout task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnCheckout, "checkout") as SvnCheckout
  }

  def "happy path"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

    when: "running the SvnCheckout task"
    task.svnUrl = localRepoUrl
    task.workspaceDir = workspaceDir
    task.execute()

    then: "local workspace exists"
    getRevision(workspaceDir) == 1
  }

  def "invalid remote URL"() {
    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/blah"
    task.workspaceDir = new File(tempDir, "workspace")
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
    task.workspaceDir = workspaceDir
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
    e.cause.message == "workspaceDir must be specified"
  }

  def "checkout with proxy"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()
    project.extensions.findByType(SvnToolsPluginExtension).proxy.host = "localhost"
    project.extensions.findByType(SvnToolsPluginExtension).proxy.port = 9999

    when: "running the SvnCheckout task"
    task.svnUrl = localRepoUrl
    task.workspaceDir = workspaceDir
    task.execute()

    then: "local workspace exists"
    getRevision(workspaceDir) == 1
  }

  def "checkout into a existing workspace"() {
    given: "an SVN workspace"
    def workspaceDir = checkoutTrunk()

    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.update = true
    task.execute()

    then: "local workspace exists"
    getRevision(workspaceDir) == 1
  }

  def "checkout into a existing workspace without update-flag"() {
    given: "an SVN workspace"
    def workspaceDir = checkoutTrunk()

    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.execute()

    then:
    def e = thrown TaskExecutionException
    e.cause.message =~ ".* must be an empty directory"
  }

  def "checkout into a wrong workspace"() {
    given: "an SVN workspace"
    def workspaceDir = checkoutBranch()

    when: "running the SvnCheckout task for a different location"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.update = true
    task.execute()

    then:
    def e = thrown TaskExecutionException
    e.cause.message =~ "SVN location of .* is invalid: .*"
  }
}
