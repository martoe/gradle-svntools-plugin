package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.api.SvnVersionData

import static org.tmatesoft.svn.core.SVNDepth.*

class SvnVersionTest extends SvnWorkspaceTestSupport {

  def "single revision"() {
    given: "an SVN workspace at a single revision"

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.targetPropertyName = "myVersion"
    task.run()

    then: "SVN version contains the single revision"
    def version = project.ext.myVersion as SvnVersionData
    version != null
    version as String == "1"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == false
    version.sparse == false
  }

  def "mixed revision"() {
    given: "an SVN workspace with mixed revision"
    switchLocalRepo("/")
    addFile("trunk/newfile.txt")
    addFile("trunk/newfile2.txt")
    update("trunk")

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "SVN version contains mixed revision"
    def version = project.ext.svnVersion as SvnVersionData
    version != null
    version as String == "1:3"
    version.mixedRevision == true
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 3
    version.modified == false
  }

  def "workspace modification"() {
    given: "an SVN workspace at a single revision"
    switchLocalRepo("/")
    new File(workspace, "trunk/test.txt").text = "modified content"

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.targetPropertyName = "myVersion"
    task.run()

    then: "SVN version contains the single revision"
    def version = project.ext.myVersion as SvnVersionData
    version != null
    version as String == "1M"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == true
  }

  def "switched workspace"() {
    given: "a switched SVN workspace"
    switchLocalRepo("/")
    switchLocalRepo("trunk", "branches")

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "SVN version contains a switched workspace"
    def version = project.ext.svnVersion as SvnVersionData
    version != null
    version as String == "1S"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == false
    version.switched == true
  }

  def "unversioned file"() {
    given: "an SVN workspace with an unversioned file"
    newFile("newfile.txt")

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "no modification"
    project.ext.svnVersion as String == "1"
  }

  def "added dir"() {
    given: "an SVN workspace with an added dir"
    clientManager.WCClient.doAdd(newFile("newdir/newfile.txt").parentFile, false, false, false, EMPTY, false, false)

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "modification"
    project.ext.svnVersion as String == "1M"
  }

  def "locally deleted file"() {
    given: "an SVN workspace with a deleted file"
    existingFile("test.txt").delete()

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "modification"
    project.ext.svnVersion as String == "1M"
  }

  def "file marked for deletion"() {
    given: "an SVN workspace with a deleted file"
    clientManager.WCClient.doDelete(existingFile("test.txt"), false, false, false)

    when: "running the SvnVersion task"
    def task = taskWithType(SvnVersion)
    task.sourcePath = workspace
    task.run()

    then: "modification"
    project.ext.svnVersion as String == "1M"
  }
}
