package at.bxm.gradleplugins.svntools

class SvnToolsPluginTest extends SvnTestSupport {

  def "apply plugin"() {
    when: "applying the plugin"
    def project = projectWithPlugin()

    then: "tasks are defined"
    project.tasks.size() == 3
    project.tasks["svnInfo"] instanceof SvnInfo
    project.tasks["svnCommit"] instanceof SvnCommit
    project.tasks["svnTag"] instanceof SvnTag
  }
}
