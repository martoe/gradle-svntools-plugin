package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import at.bxm.gradleplugins.svntools.TestSvnStatusHandler
import at.bxm.gradleplugins.svntools.internal.SvnBaseTask
import org.gradle.api.Project
import org.tmatesoft.svn.core.SVNDepth
import org.tmatesoft.svn.core.wc.SVNRevision

abstract class SvnWorkspaceTestSupport extends SvnTestSupport {

  File workspace
  Project project

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
  }

  protected <T extends SvnBaseTask> T taskWithType(Class<T> taskClass) {
    return project.task(type: taskClass, taskClass.simpleName.toLowerCase())
  }

  File newFile(String name, File path = workspace) {
    def file = new File(path, name)
    assert !file.exists(), "$file.absolutePath already exist"
    file.parentFile.mkdirs()
    file.text = "new file"
    return file
  }

  File existingFile(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    assert file.file, "$file.absolutePath isn't a file"
    return file
  }

  File existingDir(String name) {
    def file = new File(workspace, name)
    assert file.exists(), "$file.absolutePath doesn't exist"
    assert file.directory, "$file.absolutePath isn't a directory"
    return file
  }
  
  TestSvnStatusHandler status(File rootPath = workspace) {
    def statusHandler = new TestSvnStatusHandler()
    clientManager.statusClient.doStatus(rootPath, SVNRevision.UNDEFINED, SVNDepth.INFINITY, false, true, false, false, statusHandler, null)
    return statusHandler
  }
}
