package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.tasks.TaskExecutionException

class SvnDeleteTest extends SvnWorkspaceTestSupport {

  def "deleting a file"() {
    given:
    def existingFile = existingFile("test.txt")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(existingFile)
    task.execute()

    then: "file deleted"
    status().deleted == [existingFile]
  }

  def "deleting a directory"() {
    given:
    def dir = existingDir("dir")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(dir)
    task.execute()

    then: "dir deleted"
    status().deleted == [dir]
  }
  
  def "deleting a dirty directory"() {
    given:
    def dir = existingDir("dir")
    new File(dir, "unversioned.txt").text = "x"

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(dir)
    task.execute()

    then: "exception"
    def exception = thrown TaskExecutionException
    exception.cause.message.readLines()[0] =~ "svn: E200005: .* is not under version control"
  }

  def "force deletion a dirty directory"() {
    given:
    def dir = existingDir("dir")
    new File(dir, "unversioned.txt").text = "x"

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.ignoreErrors = true
    task.delete(dir)
    task.execute()

    then: "dir deleted"
    status().deleted == [dir]
  }
}
