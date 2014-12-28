package at.bxm.gradleplugins.svntools

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class SvnToolsPluginTest extends Specification {

  Project project

  Project projectWithPlugin() {
    project = ProjectBuilder.builder().build()
    project.apply plugin: "at.bxm.svntools"
    return project
  }

  def "apply plugin"() {
    expect:
    projectWithPlugin() != null
  }
}
