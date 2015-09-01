## SvnBranch task (at.bxm.gradleplugins.svntools.tasks.SvnBranch)

Creates an SVN branch based on a local SVN workspace or a remote repository.
This task requires the standard SVN directory layout (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

Property        | Description | Default value
--------------- | ----------- | -------------
svnUrl          | The repository URL that should be branched. This must point to either a trunk, a branch, or a tag.<br>Currently, tags are always created from `HEAD`.<br>If this property is missing, the local workspace will be branched instead (see `workspaceDir` below). |
workspaceDir    | The local workspace that should be branched.<br>Will only be used if the `svnUrl` property  is missing.  | `$project.projectDir`
branchName      | The name of the SVN branch (required) |
replaceExisting | If the branch already exists, delete it first | `false`)
commitMessage   | A commit message (optional) |
username        | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password        | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
