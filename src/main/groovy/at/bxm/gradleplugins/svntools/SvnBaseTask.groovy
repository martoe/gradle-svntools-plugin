package at.bxm.gradleplugins.svntools

import org.gradle.api.DefaultTask
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions
import org.tmatesoft.svn.core.wc.SVNClientManager

abstract class SvnBaseTask extends DefaultTask {

  /** The SVN username - leave empty if no authentication is required (default: {@code project.svntools.username}) */
  String username
  /** The SVN password - leave empty if no authentication is required (default: {@code project.svntools.password}) */
  String password

  SVNClientManager createSvnClientManager() {
    def username = getUsername()
    if (username) {
      return SVNClientManager.newInstance(
          // create a local SVN config dir to make sure we don't reuse existing credentials:
          new DefaultSVNOptions(new File(".subversion"), true),
          username, getPassword())
    } else {
      return SVNClientManager.newInstance()
    }
  }

  String getUsername() {
    if (username) {
      return username
    }
    return project.hasProperty("svntools.username") ? project.property("svntools.username") as String : null
  }

  String getPassword() {
    if (password) {
      return password
    }
    return project.hasProperty("svntools.password") ? project.property("svntools.password") as String : null
  }
}
