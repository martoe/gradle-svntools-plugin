package at.bxm.gradleplugins.svntools

class SvnToolsPluginTest extends SvnTestSupport {

  def "apply plugin"() {
    when: "applying the plugin"
    def project = projectWithPlugin()

    then: "extension is defined"
    project.extensions.getByType(SvnToolsPluginExtension)
  }
}
