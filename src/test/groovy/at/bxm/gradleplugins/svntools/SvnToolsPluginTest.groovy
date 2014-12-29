package at.bxm.gradleplugins.svntools

import spock.lang.Specification

class SvnToolsPluginTest extends Specification implements SvnTestSupport {

  def "apply plugin"() {
    when: "applying the plugin"
    def project = projectWithPlugin()

    then: "tasks are defined"
    projectWithPlugin().tasks.size() == 1
    projectWithPlugin().tasks["svnInfo"] instanceof SvnInfo
  }
}
