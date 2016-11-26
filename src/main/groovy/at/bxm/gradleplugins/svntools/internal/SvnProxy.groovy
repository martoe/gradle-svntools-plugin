package at.bxm.gradleplugins.svntools.internal

import groovy.transform.ToString

@ToString(includePackage = false, excludes = "password")
class SvnProxy {
  String host
  int port
  String username
  char[] password
  {
    host = System.getProperty("http.proxyHost")
    port = (System.getProperty("http.proxyPort") ?: -1) as int
    username = System.getProperty("http.proxyUser")
    password = System.getProperty("http.proxyPassword") ? System.getProperty("http.proxyPassword").chars : null
  }
}
