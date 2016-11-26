package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.tasks.TaskExecutionException

class SvnBranchTest extends SvnWorkspaceTestSupport {

  def "branch from trunk"() {
    given: "a trunk workspace"

    when: "running the task"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.branchName = "branch"
    task.execute()

    then: "branch exists"
    switchLocalRepo("branches/branch")
    getRevision(workspace) == 2
  }

  def "branch from a branch"() {
    given: "a branch workspace"
    switchLocalRepo("branches/test-branch")

    when: "running the task"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.branchName = "v2.0"
    task.execute()

    then: "branch exists"
    switchLocalRepo("branches/v2.0")
    getRevision(workspace) == 2
  }

  def "branch already exists"() {
    given: "a trunk workspace"

    when: "creating an existing branch"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.branchName = "test-branch"
    task.execute()

    then:
    def e = thrown TaskExecutionException
    e.cause.message =~ /.* already exists/
  }

  def "branch already exists - replace"() {
    given: "a trunk workspace"

    when: "creating an existing branch"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.branchName = "test-branch"
    task.replaceExisting = true
    task.execute()

    then: "two more commits (delete and copy)"
    switchLocalRepo("branches/test-branch")
    getRevision(workspace) == 3
  }

  def "no branch name"() {
    when: "running the task without branch name"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.execute()

    then: "exception"
    def exception = thrown TaskExecutionException
    exception.cause.message.readLines()[0] == "branchName missing"
  }

  def "invalid branch name"() {
    when: "running the task with an invalid branch name"
    def task = taskWithType(SvnBranch)
    task.workspaceDir = workspace
    task.branchName = ":invalid:"
    task.execute()

    then: "exception"
    def exception = thrown TaskExecutionException
    exception.cause.message.readLines()[0] == "branchName contains invalid chars: :invalid:"
  }
}
