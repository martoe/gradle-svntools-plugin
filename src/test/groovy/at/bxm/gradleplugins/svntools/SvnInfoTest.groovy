package at.bxm.gradleplugins.svntools

import spock.lang.Specification

class SvnInfoTest extends Specification implements SvnTestSupport {

  def "happy path"() {
    given:
    projectWithPlugin()
    def task = project.task([type: SvnInfo], "myTask") as SvnInfo
    // TODO prepare a SVN workspace

    when: "configuring and running the SvnInfo task"
    task.sourceDir = new File(".").absolutePath
    task.execute()

    then: "SVN data are available"
    project.ext.svnData != null
    // TODO further asserts
  }
}
