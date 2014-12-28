package at.bxm.gradleplugins.svntools

import org.gradle.api.DefaultTask
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions
import org.tmatesoft.svn.core.wc.SVNClientManager

abstract class SvnBaseTask extends DefaultTask {

  def svnUsername = System.getProperty("svnUsername")
  def svnPassword = System.getProperty("svnPassword")

  SVNClientManager createSvnClientManager() {
    if (svnUsername) {
      return SVNClientManager.newInstance(
          // create a local SVN config dir to make sure we don't reuse existing credentials:
          new DefaultSVNOptions(new File(".subversion"), true),
          svnUsername, svnPassword)
    } else {
      return SVNClientManager.newInstance()
    }
  }
}
