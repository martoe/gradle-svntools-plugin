package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.api.SvnData
import org.gradle.api.InvalidUserDataException

class SvnInfoTest extends SvnTestSupport {

  def "execute at trunk"() {
    given: "an SVN workspace at trunk"
    createLocalRepo()
    def workspace = checkoutTrunk()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "SVN data are available"
    def svnData = project.ext.svnData as SvnData
    svnData != null
    svnData.trunk
    svnData.name == "trunk"
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == 1
  }

  def "execute at a branch"() {
    given: "an SVN workspace at a branch"
    createLocalRepo()
    def workspace = checkoutBranch()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "SVN data are available"
    def svnData = project.ext.svnData as SvnData
    svnData != null
    !svnData.trunk
    svnData.branch
    svnData.name == "test-branch"
    !svnData.tag
    svnData.revisionNumber == 1
  }

  def "execute at a tag"() {
    given: "an SVN workspace at a tag"
    createLocalRepo()
    def workspace = checkoutTag()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "SVN data are available"
    def svnData = project.ext.svnData as SvnData
    svnData != null
    !svnData.trunk
    !svnData.branch
    svnData.tag
    svnData.name == "test-tag"
    svnData.revisionNumber == 1
  }

  def "use custom property name"() {
    given: "an SVN workspace"
    createLocalRepo()
    def workspace = checkoutTrunk()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = workspace
    task.targetPropertyName = "myProp"
    task.run()

    then: "SVN data are available with the right name"
    project.hasProperty("myProp")
    !project.hasProperty("svnData")
  }

  def "execute on a single file at trunk"() {
    given: "an SVN workspace at trunk"
    createLocalRepo()
    def workspace = checkoutTrunk()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = new File(workspace, "test.txt")
    task.run()

    then: "SVN data are available"
    project.ext.svnData.name == "trunk"
  }

  def "execute on a single file at a branch"() {
    given: "an SVN workspace at a branch"
    createLocalRepo()
    def workspace = checkoutBranch()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = new File(workspace, "test.txt")
    task.run()

    then: "SVN data are available"
    project.ext.svnData.name == "test-branch"
  }

  def "execute on a single file at a tag"() {
    given: "an SVN workspace at a tag"
    createLocalRepo()
    def workspace = checkoutTag()

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = new File(workspace, "test.txt")
    task.run()

    then: "SVN data are available"
    project.ext.svnData.name == "test-tag"
  }

  def "execute outside of a workspace"() {
    given: "no SVN workspace"

    when: "running the SvnInfo task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnInfo, "info") as SvnInfo
    task.sourcePath = tempDir
    task.run()

    then: "SVN data are available"
    thrown InvalidUserDataException
  }

  // https://github.com/martoe/gradle-svntools-plugin/issues/11
  def "different revisions on branches"() {
    given: "an SVN repo at revision 1"
    createLocalRepo()
    def project = projectWithPlugin()
    def workspace = checkoutTrunk()

    when: "checking out trunk"
    def task = project.task(type: SvnInfo, "info1") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "trunk is at revision 1"
    project.ext.svnData.revisionNumber == 1

    when: "committing to trunk"
    addFile("trunk/somefile.txt")
    updateLocalRepo()
    task = project.task(type: SvnInfo, "info2") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "trunk is at revision 2"
    project.ext.svnData.revisionNumber == 2

    when: "switching to root"
    switchLocalRepo("/")
    task = project.task(type: SvnInfo, "info3") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "root is at revision 2"
    project.ext.svnData.revisionNumber == 2

    when: "switching to branch"
    switchLocalRepo("branches/test-branch")
    task = project.task(type: SvnInfo, "info4") as SvnInfo
    task.sourcePath = workspace
    task.run()

    then: "branch is still at revision 1"
    project.ext.svnData.revisionNumber == 1
  }
}
