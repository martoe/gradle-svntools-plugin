package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.Project

/*
 * Copyright (c) 2016 1&1 Mail & Media GmbH, Muenchen. All rights reserved.
 */

/**
 * Do all the tests for SvnTag plus some extra ...
 */
class SvnTagDirty extends SvnTagTest {


  def setup() {
    task.localChanges = true
  }

  def "tag on dirty workspace" () {
    given: "a dirty workspace"
    workspace = checkoutTrunk()
    def textFile = new File(workspace, "test.txt")
    textFile.write("Dirty !")

    when: "running the SvnTag task"
    task.tagName = "dirty-tag"
    task.workspaceDir = workspace
    task.execute()

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
    task.localChanges = false
    task.workspaceDir = workspace
    task.execute()

    then: "tag does not contain changed file"
    workspace.deleteDir()
    def tag = checkoutLocalRepo("tags/clean-tag")
    def branchFile = new File(tag, "test.txt")
    branchFile.text == ""
  }
}
