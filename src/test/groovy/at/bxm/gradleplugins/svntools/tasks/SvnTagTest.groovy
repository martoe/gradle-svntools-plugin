package at.bxm.gradleplugins.svntools.tasks

import org.gradle.api.InvalidUserDataException
import org.tmatesoft.svn.core.io.SVNRepositoryFactory

class SvnTagTest extends SvnWorkspaceTestSupport {

  def "no tagName"() {
    given: "a workspace"

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.run()

    then: "exception"
    def e = thrown InvalidUserDataException
    e.message == "tagName missing"
  }

  def "tag from trunk"() {
    given: "a trunk workspace"

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = "my-tag"
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/my-tag")
    getRevision(workspace) == 2
  }

  def "tag from a branch"() {
    given: "a branch workspace"
    switchLocalRepo("branches/test-branch")

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = "my.tag"
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/my.tag")
    getRevision(workspace) == 2
  }

  def "tag from a tag"() {
    given: "a tag workspace"
    switchLocalRepo("tags/test-tag")

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = "myTag"
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/myTag")
    getRevision(workspace) == 2
  }

  def "invalid tagName"() {
    given: "a workspace"

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = param
    task.run()

    then: "exception"
    def e = thrown InvalidUserDataException
    e.message == "tagName contains invalid chars: $param" as String

    where:
    param << ["blank ", "backslash\\"]
  }

  def "tagName with special chars"() {
    given: "a workspace"

    when: "running the SvnTag task"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = param
    task.specialChars = true
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/$param")
    getRevision(workspace) == 2

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
    updateLocalRepo()

    when: "running the SvnTag task on that subdirectory"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = "tag-subdir/new-tag"
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/tag-subdir/new-tag")
    getRevision(workspace) == 3
  }

  def "tag into non-existing subdirectory"() {
    given: "a trunk workspace"

    when: "running the SvnTag task no a non-existing subdirectory"
    def task = taskWithType(SvnTag)
    task.workspaceDir = workspace
    task.tagName = "tag-subdir/new-tag"
    task.run()

    then: "tag exists"
    switchLocalRepo("tags/tag-subdir/new-tag")
    getRevision(workspace) == 2
  }
}
