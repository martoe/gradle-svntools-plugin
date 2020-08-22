package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project

class SvnUpdateTest extends SvnTestSupport {

  File workspace
  Project project
  SvnUpdate task

  def setup() {
    createLocalRepo()
    addFile("trunk/revision2.txt")
    workspace = checkoutTrunk()
    addFile("trunk/revision3.txt")
    project = projectWithPlugin()
    task = project.task(type: SvnUpdate, "task") as SvnUpdate
    task.workspaceDir = workspace
    // now, the server is at rev3 and the workspace is at rev2
  }

  def "happy path"() {
    when: "updating to HEAD"
    task.run()

    then: "update succeeded"
    getRevision(workspace) == 3
  }

  def "update to revision"() {
    when: "updating to revision"
    task.revision = 1
    task.run()

    then: "update succeeded"
    getRevision(workspace) == 1
  }

  def "update into a non-repo dir"() {
    given: "a non-repo dir"
    def workspaceDir = new File(tempDir, "myNewWorkspace")
    workspaceDir.mkdir()

    when: "updating"
    task.workspaceDir = workspaceDir
    task.run()

    then:
    def e = thrown InvalidUserDataException
    e.message =~ ".* E155007: None of the targets are working copies"
  }
}
