package at.bxm.gradleplugins.svntools

import spock.lang.Specification

class SvnToolsPluginTest extends Specification implements SvnTestSupport {

  def "apply plugin"() {
    expect:
    projectWithPlugin() != null
  }
}
