package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project

/**
 * Do all the tests for SvnTag plus some extra ...
 */
class SvnTagDirty extends SvnTestSupport {

  File workspace
  Project project
  SvnTag task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnTag, "tagging") as SvnTag
  }

  def "tag on dirty workspace" () {
    given: "a dirty workspace"
    workspace = checkoutTrunk()
    def textFile = new File(workspace, "test.txt")
    textFile.write("Dirty !")

    when: "running the SvnTag task"
    task.tagName = "dirty-tag"
    task.localChanges = true
    task.workspaceDir = workspace
    task.run()

    then: "tag contains changed file"
    workspace.deleteDir()
    def tag = checkoutLocalRepo("tags/dirty-tag")
    def branchFile = new File(tag, "test.txt")
    branchFile.text == "Dirty !"
    tag.deleteDir()
    def cleanWorkspace = checkoutTrunk()
    def cleanFile = new File(cleanWorkspace, "test.txt")
    cleanFile.text == ""
  }

  def "clean tag on dirty workspace" () {
    given: "a dirty workspace"
    workspace = checkoutTrunk()
    def textFile = new File(workspace, "test.txt")
    textFile.write("Dirty !")

    when: "running the SvnTag task without local changes"
    task.tagName = "clean-tag"
    task.workspaceDir = workspace
    task.run()

    then: "tag does not contain changed file"
    workspace.deleteDir()
    def tag = checkoutLocalRepo("tags/clean-tag")
    def branchFile = new File(tag, "test.txt")
    branchFile.text == ""
  }
}
