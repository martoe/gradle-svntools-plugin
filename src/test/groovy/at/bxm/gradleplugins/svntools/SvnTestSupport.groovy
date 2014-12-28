package at.bxm.gradleplugins.svntools

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

trait SvnTestSupport {

  Project project

  Project projectWithPlugin() {
    project = ProjectBuilder.builder().build()
    project.apply plugin: "at.bxm.svntools"
    return project
  }
}
