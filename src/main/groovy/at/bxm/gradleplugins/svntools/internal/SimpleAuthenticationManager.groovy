package at.bxm.gradleplugins.svntools.internal

import groovy.util.logging.Log
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager
import org.tmatesoft.svn.core.auth.ISVNProxyManager
import org.tmatesoft.svn.core.auth.SVNAuthentication
import org.tmatesoft.svn.core.auth.SVNPasswordAuthentication
import org.tmatesoft.svn.core.auth.SVNSSHAuthentication
import org.tmatesoft.svn.core.auth.SVNSSLAuthentication
import org.tmatesoft.svn.core.auth.SVNUserNameAuthentication
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions

/**
 * Adds "nonProxyHosts" support to the BasicAuthenticationManager
 */
@Log
class SimpleAuthenticationManager extends BasicAuthenticationManager {

  private String nonProxyHosts
  
  SimpleAuthenticationManager(String username, char[] password) {
    super([
      // the first three arguments are copied from BasicAuthenticationManager.newInstance(String, char[])
      SVNPasswordAuthentication.newInstance(username, password, false, null, false),
      SVNSSHAuthentication.newInstance(username, password, -1, false, null, false),
      SVNUserNameAuthentication.newInstance(username, false, null, false),
      // the forth argument is needed when accessing a repo via https
      SVNSSLAuthentication.newInstance(null as File, "dummy".toCharArray(), false, null, false)
    ] as SVNAuthentication[])
  }

  void setProxy(String proxyHost, int proxyPort, String proxyUserName, char[] proxyPassword, String nonProxyHosts) {
    super.setProxy(proxyHost, proxyPort, proxyUserName, (char[])proxyPassword)
    this.nonProxyHosts = nonProxyHosts
  }

  @Override
  ISVNProxyManager getProxyManager(SVNURL url) throws SVNException {
    if (!proxyHost) {
      log.fine("No proxy configured for $url")
      return null
    } else if (hostExceptedFromProxy(url.host)) {
      log.fine("Bypassing proxy for $url")
      return null
    } else {
      log.fine("Using proxy for $url")
      return this
    }
  }

  /** @see org.tmatesoft.svn.core.internal.wc.DefaultSVNHostOptions */
  private boolean hostExceptedFromProxy(String host) {
    if (nonProxyHosts) {
      for (def exceptions = new StringTokenizer(nonProxyHosts, "|"); exceptions.hasMoreTokens();) {
        def exception = exceptions.nextToken().trim()
        if (DefaultSVNOptions.matches(exception, host)) {
          return true
        }
      }
    }
    return false
  }
}
