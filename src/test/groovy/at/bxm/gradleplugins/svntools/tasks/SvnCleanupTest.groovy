package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.InvalidUserDataException

class SvnCleanupTest extends SvnWorkspaceTestSupport {

  def "cleanup two directories"() {
    given: "an SVN working copy"

    when: "running the SvnCleanup task"
    def task = taskWithType(SvnCleanup)
    task.cleanup(workspace, new File(workspace, "dir"))
    task.run()

    then: "no error"
  }

  def "cleanup a file"() {
    given:
    def workspaceFile = existingFile("test.txt")

    when: "running the SvnCleanup task"
    def task = taskWithType(SvnCleanup)
    task.cleanup = workspaceFile
    task.run()

    then: "exception"
    def e = thrown InvalidUserDataException
    e.message =~ "Not a directory: .*"
  }

  def "cleanup a non-working-copy directory"() {
    given:
    def unversioned = new File(workspace, "newDir")
    unversioned.mkdir()

    when: "running the SvnCleanup task"
    def task = taskWithType(SvnCleanup)
    task.cleanup(unversioned)
    task.run()

    then: "exception"
    def e = thrown InvalidUserDataException
    def msg = e.message.readLines()
    msg[0] =~ "svn-cleanup failed for .*"
    msg[1] =~ "svn: E155007: .* is not a working copy directory"
  }
}
