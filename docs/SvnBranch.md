## SvnBranch task (at.bxm.gradleplugins.svntools.tasks.SvnBranch)

Creates an SVN branch based on a local SVN workspace.
This task requires the standard SVN directory layout (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

Property        | Description | Default value
--------------- | ----------- | -------------
workspaceDir    | Local workspace that should be tagged | `$project.projectDir`
branchName      | Name of the SVN branch (required) |
replaceExisting | If the branch already exists, delete it first | `false`)
commitMessage   | A commit message (optional) |
username        | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password        | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
