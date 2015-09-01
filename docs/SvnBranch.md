## SvnBranch task (at.bxm.gradleplugins.svntools.tasks.SvnBranch)

Creates an SVN branch based on a local SVN workspace or a remote repository.
This task requires the standard SVN directory layout (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

Property        | Description | Default value
--------------- | ----------- | -------------
svnUrl          | A SVN repository URL that should be tagged. This must point to either a trunk, a branch, or a tag.<br>Currently, tags are always created from `HEAD`.<br>If missing, the local `workspaceDir` will be used. |
workspaceDir    | Local workspace that should be tagged | `$project.projectDir`
branchName      | Name of the SVN branch (required) |
replaceExisting | If the branch already exists, delete it first | `false`)
commitMessage   | A commit message (optional) |
username        | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password        | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
