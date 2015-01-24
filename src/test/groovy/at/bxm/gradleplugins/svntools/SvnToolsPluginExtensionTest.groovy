package at.bxm.gradleplugins.svntools

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
}
