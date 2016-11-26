package at.bxm.gradleplugins.svntools.internal

import at.bxm.gradleplugins.svntools.SvnToolsPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.tmatesoft.svn.core.wc.SVNClientManager

abstract class SvnBaseTask extends DefaultTask {

  /** The SVN username - leave empty if no authentication is required (default: {@code project.svntools.username}) */
  @Internal String username
  /** The SVN password - leave empty if no authentication is required (default: {@code project.svntools.password}) */
  @Internal char[] password

  SVNClientManager createSvnClientManager() {
    return SvnSupport.createSvnClientManager(getUsername(), getPassword(), proxy)
  }

  String getUsername() {
    return username ?: project.extensions.getByType(SvnToolsPluginExtension).username
  }

  char[] getPassword() {
    return password ?: project.extensions.getByType(SvnToolsPluginExtension).password
  }

  @Internal
  SvnProxy getProxy() {
    return project.extensions.getByType(SvnToolsPluginExtension).proxy
  }
}
