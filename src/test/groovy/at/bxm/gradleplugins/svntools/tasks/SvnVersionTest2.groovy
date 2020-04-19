package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.api.SvnVersionData
import org.tmatesoft.svn.core.SVNDepth

class SvnVersionTest2 extends SvnTestSupport {

  def "sparse working copy"() {
    given: "a sparsely populated SVN working copy"
    createLocalRepo()
    def workspace = checkoutLocalRepo("/", SVNDepth.IMMEDIATES)

    when: "running the SvnVersion task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = workspace
    task.run()

    then: "SVN version contains a sparse working copy"
    def version = project.ext.svnVersion as SvnVersionData
    version != null
    version as String == "1P"
    version.mixedRevision == false
    version.minRevisionNumber == 1
    version.maxRevisionNumber == 1
    version.modified == false
    version.switched == false
    version.sparse == true
  }

  def "no working copy"() {
    when: "running the SvnVersion task without working copy"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = tempDir
    task.ignoreErrors = true
    task.run()

    then: "SVN version contains a sparse working copy"
    def version = project.ext.svnVersion as SvnVersionData
    version != null
    version as String == "exported"
  }

  def "externals"() { // see https://github.com/martoe/gradle-svntools-plugin/issues/23
    given: "an SVN workspace with an external definition"
    createLocalRepoWithExternals()
    def workspace = checkoutLocalRepo("/")

    when: "running the SvnVersion task"
    def project = projectWithPlugin()
    def task = project.task(type: SvnVersion, "version") as SvnVersion
    task.sourcePath = workspace
    task.targetPropertyName = "myVersion"
    task.run()

    then: "SVN version contains no modification"
    def version = project.ext.myVersion as SvnVersionData
    version != null
    version as String == "1:2" // mixed because the external contains rev.1
    version.modified == false
  }
}
