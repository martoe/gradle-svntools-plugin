package at.bxm.gradleplugins.svntools

class SvnToolsPluginTest extends SvnTestSupport {

  def "apply plugin"() {
    when: "applying the plugin"
    def project = projectWithPlugin()

    then: "tasks are defined"
    projectWithPlugin().tasks.size() == 2
    projectWithPlugin().tasks["svnInfo"] instanceof SvnInfo
    projectWithPlugin().tasks["svnCommit"] instanceof SvnCommit
  }
}
