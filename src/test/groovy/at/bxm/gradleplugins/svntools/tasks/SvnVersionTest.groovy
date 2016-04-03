package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.api.SvnVersionData

class SvnVersionTest extends SvnTestSupport {

  def "single revision"() {
    given: "an SVN workspace at a single revision"
    createLocalRepo()
    def workspace = checkoutLocalRepo("/")

    when: "running the SvnVersion task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = workspace
    task.targetPropertyName = "myVersion"
    task.execute()

    then: "SVN version contains the single revision"
    def version = project.ext.myVersion as SvnVersionData
    version != null
    version as String == "1"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == false
  }

  def "mixed revision"() {
    given: "an SVN workspace with mixed revision"
    createLocalRepo()
    def workspace = checkoutLocalRepo("/")
    addFile("trunk/newfile.txt")
    addFile("trunk/newfile2.txt")
    update("trunk")

    when: "running the SvnVersion task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = workspace
    task.execute()

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
    createLocalRepo()
    def workspace = checkoutLocalRepo("/")
    new File(workspace, "trunk/test.txt").text = "modified content"

    when: "running the SvnVersion task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = workspace
    task.targetPropertyName = "myVersion"
    task.execute()

    then: "SVN version contains the single revision"
    def version = project.ext.myVersion as SvnVersionData
    version != null
    version as String == "1M"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == true
  }
}
