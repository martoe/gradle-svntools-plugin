## [SvnTag](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnTag.groovy) task

Creates an SVN tag based on a local SVN workspace or a remote repository.
This task requires the [standard SVN directory layout](http://svnbook.red-bean.com/en/1.7/svn.branchmerge.maint.html#svn.branchmerge.maint.layout) (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

Property        | Description | Default value
--------------- | ----------- | -------------
svnUrl          | The repository URL that should be tagged. This must point to either a trunk, a branch, or a tag.<br>Tags are always created from `HEAD`.<br>If this property is missing, the local workspace will be tagged instead (see `workspaceDir` below). |
workspaceDir    | The local workspace that should be tagged.<br>Will only be used if the `svnUrl` property is missing. | `$project.projectDir`
tagName         | The name of the new SVN tag (required) |
replaceExisting | If the tag already exists, delete it first | `false`
localChanges    | If the workspace contains changes, commit those changes to the new tag | `false`
commitMessage   | A commit message (optional) |
username        | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password        | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

The `release` task creates an SVN tag using the current version number:

    apply plugin: "at.bxm.svntools"

    version = "1.0-SNAPSHOT"

    task svnTag(type: at.bxm.gradleplugins.svntools.tasks.SvnTag) {
      tagName = "v$project.version"
      commitMessage = "Release version $project.version"
    }

    task release(dependsOn: svnTag)
