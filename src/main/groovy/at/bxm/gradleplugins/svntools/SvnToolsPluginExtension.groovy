package at.bxm.gradleplugins.svntools

import at.bxm.gradleplugins.svntools.api.SvnData
import at.bxm.gradleplugins.svntools.api.SvnVersionData
import at.bxm.gradleplugins.svntools.internal.SvnProxy
import at.bxm.gradleplugins.svntools.internal.SvnSupport
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNURL

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

  /** @return svn-info for the project directory (cached) */
  SvnData getInfo() {
    if (!info) {
      info = SvnSupport.createSvnData(project.projectDir, username, password, proxy, true)
    }
    return info
  }

  /** Convenience method for receiving SVN status data for an arbitrary path (https://github.com/martoe/gradle-svntools-plugin/issues/21) */
  SvnData getInfo(File file) {
    return SvnSupport.createSvnData(file, username, password, proxy, false)
  }

  /** @return svn-version for the project directory (cached) */
  SvnVersionData getVersion() {
    if (!version) {
      version = SvnSupport.createSvnVersionData(project.projectDir, username, password, proxy, true)
    }
    return version
  }

  /** 
   * Retrieves SVN status data for a path within a remote repository
   * @param repoUrl An SVN repository URL
   * @param filePath An optional path within the repository
   */
  SvnData getRemoteInfo(String repoUrl, String filePath = null) {
    try {
      SVNURL url = SVNURL.parseURIEncoded(repoUrl)
      return SvnSupport.createSvnData(url, filePath ?: "/", username, password, proxy, false)
    } catch (SVNException e) {
      throw new InvalidUserDataException("Invalid svnUrl value: $repoUrl", e)
    }
  }
}
