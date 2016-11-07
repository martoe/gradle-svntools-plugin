package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project

class SvnApplyPatchTest extends SvnTestSupport {

  File workspace
  Project project
  SvnApplyPatch task

  def setup() {
    createLocalRepo()
    workspace = checkoutTrunk()
    project = projectWithPlugin()
    task = project.task(type: SvnApplyPatch, "applyPatch") as SvnApplyPatch
  }

  def "single file"() {
    given: "patch for a single file"
    def patchFile = new File(tempDir, "myPatch.txt")
    patchFile.text = """Index: test.txt
===================================================================
--- test.txt\t(revision 1)
+++ test.txt\t(working copy)
@@ -0,0 +1 @@
+changed
\\ No newline at end of file
"""

    when: "running the SvnApplyPatch task"
    task.patchFile = patchFile
    task.dir = workspace
    task.execute()

    then: "patch has been applied, but not committed"
    def patchedFile = new File(workspace, "test.txt")
    patchedFile.text == "changed"
    getRevision(patchedFile) == 1
  }
}
