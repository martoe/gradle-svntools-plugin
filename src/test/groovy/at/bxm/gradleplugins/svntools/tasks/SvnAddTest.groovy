package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.InvalidUserDataException

class SvnAddTest extends SvnWorkspaceTestSupport {

  def "adding a file"() {
    given: "a new file to be added to SVN"
    def newFile = newFile("newfile.txt")

    when: "running the SvnAdd task"
    def task = taskWithType(SvnAdd)
    task.add(newFile)
    task.run()

    then: "one file added"
    status().added == [newFile]
  }

  def "adding a directory without contents"() {
    given: "a directory to be added to SVN"
    def newFile = newFile("newDir/newfile.txt")
    def newDir = newFile.parentFile

    when: "running the SvnAdd task"
    def task = taskWithType(SvnAdd)
    task.add = newDir
    task.run()

    then: "just the directory added"
    def status = status()
    status.added == [newDir]
    status.unversioned == [newFile]
  }

  def "adding a directory with contents"() {
    given: "a directory to be added to SVN"
    def file = newFile("newDir/newfile.txt")
    def dir = file.parentFile
    def subFile = newFile("newDir/newDir2/newfile2.txt")
    def subDir = subFile.parentFile

    when: "running the SvnAdd task"
    def task = taskWithType(SvnAdd)
    task.add = dir
    task.recursive = true
    task.run()

    then: "everything added"
    def status = status()
    status.added == [dir, subDir, subFile, file]
    status.unversioned == []
  }

  def "no file to add - strict"() {
    when: "running the SvnAdd task without a file"
    def task = taskWithType(SvnAdd)
    task.run()

    then: "exception"
    def exception = thrown InvalidUserDataException
    exception.message.readLines()[0] == "No files to add specified"
  }

  def "no file to add - lenient"() {
    when: "running the SvnAdd task without a file"
    def task = taskWithType(SvnAdd)
    task.ignoreErrors = true
    task.run()

    then: "nothing has happened"
    status().added == []
  }
  
  def "adding a nonexisting file"() {
    when:
    def task = taskWithType(SvnAdd)
    task.add(new File(workspace, "invalid.file"))
    task.run()

    then: "exception"
    def exception = thrown InvalidUserDataException
    exception.cause.message.readLines()[0] =~ ".*svn: E155010: .* not found.*"
  }
}
