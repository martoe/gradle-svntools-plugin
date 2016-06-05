package at.bxm.gradleplugins.svntools

import at.bxm.gradleplugins.svntools.api.SvnData
import at.bxm.gradleplugins.svntools.api.SvnVersionData
import at.bxm.gradleplugins.svntools.internal.SvnProxy
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.Project

/** Holds configuration values shared by all SVN tasks and provides SVN information about the current workspace */
class SvnToolsPluginExtension {

  private final Project project
  String username
  String password
  final SvnProxy proxy = new SvnProxy()
  SvnData info
  SvnVersionData version

  SvnToolsPluginExtension(Project project) {
    this.project = project
  }

  SvnData getInfo() {
    if (!info) {
      def ext = project.extensions.getByType(SvnToolsPluginExtension)
      info = SvnSupport.createSvnData(project.projectDir, ext.username, ext.password, ext.proxy, true)
    }
    return info
  }

  /** Convenience method for receiving SVN status data for an arbitrary path (https://github.com/martoe/gradle-svntools-plugin/issues/21) */
  SvnData getInfo(File file) {
    def ext = project.extensions.getByType(SvnToolsPluginExtension)
    return SvnSupport.createSvnData(file, ext.username, ext.password, ext.proxy, false)
  }

  SvnVersionData getVersion() {
    if (!version) {
      def ext = project.extensions.getByType(SvnToolsPluginExtension)
      version = SvnSupport.createSvnVersionData(project.projectDir, ext.username, ext.password, ext.proxy, true)
    }
    return version
  }
}
