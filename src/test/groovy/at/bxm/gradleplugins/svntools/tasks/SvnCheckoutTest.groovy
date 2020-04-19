package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.SvnToolsPluginExtension
import at.bxm.gradleplugins.svntools.api.SvnDepth
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import spock.lang.Ignore

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
    task.run()

    then: "local workspace exists and contains files"
    getRevision(workspaceDir) == 1
    childrenOf(workspaceDir).size() == 3
  }

  def "invalid remote URL"() {
    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/blah"
    task.workspaceDir = new File(tempDir, "workspace")
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ "svn-checkout failed for .*"
  }

  def "checkout using a non-empty dir"() {
    given: "a non-empty workspace"
    def workspaceDir = new File(tempDir, "myNewWorkspace")
    workspaceDir.mkdirs()
    new File(workspaceDir, "test.txt").text = "placeholder"

    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ ".* must be an empty directory"
  }

  def "no target dir"() {
    when: "running the SvnCheckout task without target dir"
    task.svnUrl = localRepoUrl
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message == "workspaceDir must be specified"
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
    task.run()

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
    task.run()

    then: "local workspace exists"
    getRevision(workspaceDir) == 1
  }

  def "checkout into a existing workspace without update-flag"() {
    given: "an SVN workspace"
    def workspaceDir = checkoutTrunk()

    when: "running the SvnCheckout task"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ ".* must be an empty directory"
  }

  def "checkout into a wrong workspace"() {
    given: "an SVN workspace"
    def workspaceDir = checkoutBranch()

    when: "running the SvnCheckout task for a different location"
    task.svnUrl = "$localRepoUrl/trunk"
    task.workspaceDir = workspaceDir
    task.update = true
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ "SVN location of .* is invalid: .*"
  }

  def "checkout depth=empty"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

    when: "running the SvnCheckout task with depth=empty"
    task.svnUrl = localRepoUrl.appendPath("trunk", false)
    task.workspaceDir = workspaceDir
    task.depth = "empty"
    task.run()

    then: "local workspace is empty"
    childrenOf(workspaceDir).size() == 0
  }

  def "checkout depth=files"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

    when: "running the SvnCheckout task with depth=files"
    task.svnUrl = localRepoUrl.appendPath("trunk", false)
    task.workspaceDir = workspaceDir
    task.depth = "Files"
    task.run()

    then: "local workspace contains one file"
    def ws = childrenOf(workspaceDir)
    ws.size() == 1
    ws[0].isFile()
  }

  def "checkout depth=immediates"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

    when: "running the SvnCheckout task with depth=immediates"
    task.svnUrl = localRepoUrl.appendPath("trunk", false)
    task.workspaceDir = workspaceDir
    task.depth = SvnDepth.IMMEDIATES
    task.run()

    then: "local workspace contains one file and one empty dir"
    def ws = childrenOf(workspaceDir)
    ws.findAll({ it.isFile() }).size() == 1
    ws.findAll({ it.isDirectory() }).size() == 1
    childrenOf(ws.find({ it.isDirectory() })).size() == 0
  }

  @Ignore("expensive test with remote dependencies")
  def "remote repo"() {
    given: "nonexistent workspace"
    def workspaceDir = "$tempDir.absolutePath/myNewWorkspace"
    assert !new File(workspaceDir).exists()

    when: "running the SvnCheckout task"
    task.svnUrl = "https://svn.svnkit.com/repos/svnkit/trunk/svnkit-osgi"
    task.workspaceDir = workspaceDir
    task.run()

    then: "local workspace exists and contains files"
    getRevision(workspaceDir) > 10000
    childrenOf(workspaceDir).size() > 2
  }
  
  private static List<File> childrenOf(parentFile) {
    (parentFile as File).listFiles().findAll { it.name != ".svn" }
  }
}
