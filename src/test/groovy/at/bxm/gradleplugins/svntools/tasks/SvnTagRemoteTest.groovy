package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException

class SvnTagRemoteTest extends SvnTestSupport {

  Project project
  SvnTag task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnTag, "tagging") as SvnTag
  }

  def "remote tag from trunk"() {
    when: "running the SvnTag task without workspace"
    task.svnUrl = localRepoUrl.appendPath("trunk", false)
    task.tagName = "my-tag"
    task.execute()

    then: "tag exists"
    def workspace = checkoutLocalRepo("tags/my-tag")
    getRevision(workspace) == 2
  }

  def "remote tag without trunk-branches-tags structure"() {
    when: "running the SvnTag task from module root"
    task.svnUrl = localRepoUrl
    task.tagName = "my-tag"
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message =~ "^java.net.MalformedURLException: .*"
  }

  def "both remote and local source is set"() {
    given: "a workspace"
    def workspace = checkoutTrunk()

    when: "running the SvnTag task with local _and_ remote source"
    task.workspaceDir = workspace
    task.svnUrl = localRepoUrl
    task.tagName = "my-tag"
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message == "Either 'svnUrl' or 'workspaceDir' may be set"
  }

  def "remote tag with invalid url"() {
    when: "running the SvnTag task with an invalid url"
    task.svnUrl = "blah"
    task.tagName = "my-tag"
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message == "Invalid svnUrl value: blah"
  }

  def "remote tag with non-existing url"() {
    when: "running the SvnTag task with a non-existing url"
    task.svnUrl = "http://localhost:7654/foo/bar/trunk/"
    task.tagName = "my-tag"
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message =~ "^svn-copy failed for /foo/bar/tags/my-tag.*"
  }
}
