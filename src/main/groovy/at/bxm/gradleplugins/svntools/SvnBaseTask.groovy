package at.bxm.gradleplugins.svntools

import org.gradle.api.DefaultTask
import org.tmatesoft.svn.core.wc.SVNClientManager

abstract class SvnBaseTask extends DefaultTask {

  /** The SVN username - leave empty if no authentication is required (default: {@code project.svntools.username}) */
  String username
  /** The SVN password - leave empty if no authentication is required (default: {@code project.svntools.password}) */
  String password

  SVNClientManager createSvnClientManager() {
    return SvnSupport.createSvnClientManager(getUsername(), getPassword())
  }

  String getUsername() {
    return username ?: project.extensions.getByType(SvnToolsPluginExtension).username
  }

  String getPassword() {
    return password ?: project.extensions.getByType(SvnToolsPluginExtension).password
  }
}
