package at.bxm.gradleplugins.svntools

import org.gradle.api.Project

/** Holds configuration values shared by all SVN tasks and provides SVN information about the current workspace */
class SvnToolsPluginExtension {

  private final Project project
  String username
  String password
  SvnData info

  SvnToolsPluginExtension(Project project) {
    this.project = project
  }

  SvnData getInfo() {
    if (!info) {
      def ext = project.extensions.getByType(SvnToolsPluginExtension)
      info = SvnSupport.createSvnData(project.projectDir, ext.username, ext.password, true)
    }
    return info
  }
}
