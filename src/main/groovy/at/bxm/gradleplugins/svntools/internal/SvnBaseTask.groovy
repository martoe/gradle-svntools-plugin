package at.bxm.gradleplugins.svntools.internal

import at.bxm.gradleplugins.svntools.SvnToolsPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.tmatesoft.svn.core.wc.SVNClientManager

abstract class SvnBaseTask extends DefaultTask {

  /** The SVN username - leave empty if no authentication is required (default: {@code project.svntools.username}) */
  @Internal String username
  /** The SVN password - leave empty if no authentication is required (default: {@code project.svntools.password}) */
  private char[] password

  SVNClientManager createSvnClientManager() {
    return SvnSupport.createSvnClientManager(getUsername(), getPassword(), proxy)
  }

  String getUsername() {
    return username ?: project.extensions.getByType(SvnToolsPluginExtension).username
  }

  @Internal
  Object getPassword() {
    return password ?: project.extensions.getByType(SvnToolsPluginExtension).password
  }

  void setPassword(Object password) {
    if (password != null) {
      this.password = password instanceof char[] ?: password.toString().chars
    } else {
      this.password = null
    }
  }

  @Internal
  SvnProxy getProxy() {
    return project.extensions.getByType(SvnToolsPluginExtension).proxy
  }
}
