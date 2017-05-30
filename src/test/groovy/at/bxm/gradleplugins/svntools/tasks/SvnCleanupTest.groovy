package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.tasks.TaskExecutionException

class SvnCleanupTest extends SvnWorkspaceTestSupport {

  def "cleanup a directory"() {
    given:
    def workspaceDir = existingFile("test.txt").parentFile

    when: "running the SvnCleanup task"
    def task = taskWithType(SvnCleanup)
    task.cleanup(workspaceDir)
    task.execute()

    then: "no error"
  }

  def "cleanup a file"() {
    given:
    def workspaceFile = existingFile("test.txt")

    when: "running the SvnCleanup task"
    def task = taskWithType(SvnCleanup)
    task.cleanup = workspaceFile
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    def msg = e.cause.message.readLines()
    msg[0] =~ "svn-cleanup failed for .*"
    msg[1] =~ "svn: E155007: .* is not a working copy directory"
  }
}
