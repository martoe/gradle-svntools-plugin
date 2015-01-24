package at.bxm.gradleplugins.svntools

import org.gradle.api.*

class SvnToolsPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create("svntools", SvnToolsPluginExtension, project)
  }
}
