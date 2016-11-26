## [SvnBranch](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnBranch.groovy) task

Creates an SVN branch based on a local SVN workspace or a remote repository.
This task requires the [standard SVN directory layout](http://svnbook.red-bean.com/en/1.7/svn.branchmerge.maint.html#svn.branchmerge.maint.layout) (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

Property        | Description | Default value
--------------- | ----------- | -------------
svnUrl          | The repository URL that should be branched. This must point to either a trunk, a branch, or a tag.<br>Branches are always created from `HEAD`.<br>If this property is missing, the local workspace will be branched instead (see `workspaceDir` below). |
workspaceDir    | The local workspace that should be branched.<br>Will only be used if the `svnUrl` property is missing. | `$project.projectDir`
branchName      | The name of the new SVN branch (required) |
replaceExisting | If the branch already exists, delete it first | `false`
localChanges    | If the workspace contains changes, commit those changes to the new branch | `false`
specialChars    | Set to `true` if the target name contains arbitrary chars (as supported by the current SVN server and operation system). If `false`, only a reduced subset of chars (a-z, A-Z, 0-9, "_", "-", ".", and "/") is allowed | `false`
commitMessage   | A commit message (optional) |
username        | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password        | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
