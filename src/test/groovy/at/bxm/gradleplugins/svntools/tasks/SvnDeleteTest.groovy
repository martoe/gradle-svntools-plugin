package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.InvalidUserDataException

class SvnDeleteTest extends SvnWorkspaceTestSupport {

  def "deleting a file"() {
    given:
    def existingFile = existingFile("test.txt")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(existingFile)
    task.run()

    then: "file deleted"
    status().deleted == [existingFile]
  }

  def "deleting a non-empty directory"() {
    given:
    def dir = existingDir("dir")
    def file = new File(dir, "test.txt")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(dir)
    task.run()

    then: "dir deleted"
    status().deleted == [dir, file]
  }
  
  def "deleting a dirty directory"() {
    given:
    def dir = existingDir("dir")
    new File(dir, "unversioned.txt").text = "x"

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete(dir)
    task.run()

    then: "exception"
    def exception = thrown InvalidUserDataException
    exception.cause.message.readLines()[0] =~ "svn: E200005: .* is not under version control"
  }

  def "force deletion a dirty directory"() {
    given:
    def dir = existingDir("dir")
    def file = new File(dir, "test.txt")
    new File(dir, "unversioned.txt").text = "x"

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.ignoreErrors = true
    task.delete(dir)
    task.run()

    then: "dir deleted"
    status().deleted == [dir, file]
  }

  def "deleting a nonexisting file"() {
    given:
    def file = new File(workspace, "nonexisting.txt")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete = file
    task.run()

    then: "exception"
    def exception = thrown InvalidUserDataException
    exception.cause.message.readLines()[0] =~ "svn: E125001: .* does not exist"
  }

  def "force deletion of a nonexisting file"() {
    given:
    def file = new File(workspace, "nonexisting.txt")

    when: "running the SvnDelete task"
    def task = taskWithType(SvnDelete)
    task.delete = file
    task.ignoreErrors = true
    task.run()

    then: "no error"
  }
}
