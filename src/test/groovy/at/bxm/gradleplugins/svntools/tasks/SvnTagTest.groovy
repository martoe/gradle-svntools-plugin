package at.bxm.gradleplugins.svntools.tasks

import at.bxm.gradleplugins.svntools.SvnTestSupport
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException
import org.tmatesoft.svn.core.io.SVNRepositoryFactory

class SvnTagTest extends SvnTestSupport {

  File workspace
  Project project
  SvnTag task

  def setup() {
    createLocalRepo()
    project = projectWithPlugin()
    task = project.task(type: SvnTag, "tagging") as SvnTag
  }

  def "no tagName"() {
    given: "a workspace"
    workspace = checkoutTrunk()

    when: "running the SvnTag task"
    task.workspaceDir = workspace
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message == "tagName missing"
  }

  def "tag from trunk"() {
    given: "a trunk workspace"
    workspace = checkoutTrunk()

    when: "running the SvnTag task"
    task.workspaceDir = workspace
    task.tagName = "my-tag"
    task.execute()

    then: "tag exists"
    switchLocalRepo("tags/my-tag")
    getRevision(workspace) == 2
  }

  def "tag from a branch"() {
    given: "a branch workspace"
    workspace = checkoutBranch()

    when: "running the SvnTag task"
    task.workspaceDir = workspace
    task.tagName = "my.tag"
    task.execute()

    then: "tag exists"
    switchLocalRepo("tags/my.tag")
    getRevision(workspace) == 2
  }

  def "tag from a tag"() {
    given: "a tag workspace"
    workspace = checkoutTag()

    when: "running the SvnTag task"
    task.workspaceDir = workspace
    task.tagName = "myTag"
    task.execute()

    then: "tag exists"
    switchLocalRepo("tags/myTag")
    getRevision(workspace) == 2
  }

  def "invalid tagName"() {
    given: "a workspace"
    workspace = checkoutTrunk()

    when: "running the SvnTag task"
    task.workspaceDir = workspace
    task.tagName = param
    task.execute()

    then: "exception"
    def e = thrown TaskExecutionException
    e.cause.message == "tagName contains invalid chars: $param" as String

    where:
    param << ["blank ", "backslash\\"]
  }

  def "tag into subdirectory"() {
    given: "a repo with a 'tags' subdirectory"
    def repo = SVNRepositoryFactory.create(localRepoUrl)
    def editor = repo.getCommitEditor("creating a new file", null)
    editor.openRoot(-1)
    editor.addDir("tags/tag-subdir", null, -1)
    editor.closeDir()
    editor.closeEdit()
    workspace = checkoutTrunk()

    when: "running the SvnTag task on that subdirectory"
    task.workspaceDir = workspace
    task.tagName = "tag-subdir/new-tag"
    task.execute()

    then: "tag exists"
    switchLocalRepo("tags/tag-subdir/new-tag")
    getRevision(workspace) == 3
  }

  def "tag into non-existing subdirectory"() {
    given: "a trunk workspace"
    workspace = checkoutTrunk()

    when: "running the SvnTag task no a non-existing subdirectory"
    task.workspaceDir = workspace
    task.tagName = "tag-subdir/new-tag"
    task.execute()

    then: "tag exists"
    switchLocalRepo("tags/tag-subdir/new-tag")
    getRevision(workspace) == 2
  }
}
