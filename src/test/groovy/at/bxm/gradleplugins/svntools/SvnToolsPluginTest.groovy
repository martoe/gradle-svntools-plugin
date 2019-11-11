package at.bxm.gradleplugins.svntools

import at.bxm.gradleplugins.svntools.tasks.*

class SvnToolsPluginTest extends SvnTestSupport {

  def "apply plugin"() {
    when: "applying the plugin"
    def project = projectWithPlugin()

    then: "extension is defined"
    project.extensions.getByType(SvnToolsPluginExtension)
  }

  def "Tasks are available by name"(String name, Class<?> taskClass) {
    expect:
    def project = projectWithPlugin()
    def task = project.task(type: project.extensions.extraProperties.get(name), name.toLowerCase())
    task in taskClass

    where:
    name             | taskClass
    'SvnAdd'         | SvnAdd.class
    'SvnApplyPatch'  | SvnApplyPatch.class
    'SvnBranch'      | SvnBranch.class
    'SvnCheckout'    | SvnCheckout.class
    'SvnCleanup'     | SvnCleanup.class
    'SvnCommit'      | SvnCommit.class
    'SvnCreatePatch' | SvnCreatePatch.class
    'SvnDelete'      | SvnDelete.class
    'SvnExport'      | SvnExport.class
    'SvnInfo'        | SvnInfo.class
    'SvnRevert'      | SvnRevert.class
    'SvnTag'         | SvnTag.class
    'SvnUpdate'      | SvnUpdate.class
    'SvnVersion'     | SvnVersion.class
  }
}
