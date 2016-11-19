package at.bxm.gradleplugins.svntools

import org.gradle.api.InvalidUserDataException

class SvnToolsPluginExtensionTest extends SvnTestSupport {

  def "access svnData at trunk"() {
    given: "an SVN workspace at trunk"
    createLocalRepo()
    def workspace = checkoutTrunk()

    expect: "valid SvnInfo object"
    def svnData = projectWithPlugin(workspace).extensions.getByType(SvnToolsPluginExtension).info
    svnData != null
    svnData.trunk
    svnData.name == "trunk"
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == 1
    svnData.committedDate != null
    svnData.committedAuthor == "username"
  }

  def "access svnData outside a workspace"() {
    given: "no SVN workspace"

    expect: "invalid SvnInfo object"
    def svnData = projectWithPlugin().extensions.getByType(SvnToolsPluginExtension).info
    svnData != null
    !svnData.trunk
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == -1
  }

  def "access svnData for an arbitrary file"() {
    given: "an SVN workspace at trunk"
    createLocalRepo()
    def workspace = checkoutTrunk()

    when: "reading SvnData for a file"
    def svnData = projectWithPlugin(workspace).extensions.getByType(SvnToolsPluginExtension).getInfo(new File(workspace, "test.txt"))
    
    then: "data returned"
    svnData != null
    svnData.trunk
    svnData.name == "trunk"
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == 1
  }

  def "access svnData for a non-existing file"() {
    given: "an SVN workspace at trunk"
    createLocalRepo()
    def workspace = checkoutTrunk()

    when: "reading SvnData for a non-existing file"
    projectWithPlugin(workspace).extensions.getByType(SvnToolsPluginExtension).getInfo(new File(workspace, "missing.txt"))
    
    then: "error"
    thrown(InvalidUserDataException)
  }

  def "access svnData for a remote repository"() {
    given: "an SVN repo"
    def url = createLocalRepo() as String

    when: "reading SvnData for the repository"
    def svnData = projectWithPlugin().extensions.getByType(SvnToolsPluginExtension).getRemoteInfo(url)
    
    then: "data returned"
    svnData != null
    svnData.name == null
    !svnData.trunk
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == 1
  }

  def "access svnData for a remote file"() {
    given: "an SVN repo"
    def url = createLocalRepo() as String

    when: "reading SvnData for a remote file"
    def svnData = projectWithPlugin().extensions.getByType(SvnToolsPluginExtension).getRemoteInfo(url, "trunk/test.txt")
    
    then: "data returned"
    svnData != null
    svnData.trunk
    svnData.name == "trunk"
    !svnData.branch
    !svnData.tag
    svnData.revisionNumber == 1
  }

  def "access svnData for a non-existing remote file"() {
    given: "an SVN repo"
    def url = createLocalRepo() as String

    when: "reading SvnData for a non-existing remote file"
    def svnData = projectWithPlugin().extensions.getByType(SvnToolsPluginExtension).getRemoteInfo(url, "trunk/invalid.txt")
    
    then: "error"
    thrown(InvalidUserDataException)
  }
}
