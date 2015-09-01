## SvnBranch task (at.bxm.gradleplugins.svntools.tasks.SvnBranch)

Creates an SVN branch based on a local SVN workspace.
This task requires the standard SVN directory layout (`[module]/trunk`, `[module]/branches/[branch]`, `[module]/tags/[tag]`).

### Configuration

* **workspaceDir**: Local workspace that should be tagged (default: `$project.projectDir`)
* **branchName**: Name of the SVN branch (required, no default)
* **replaceExisting**: If the branch already exists, delete it first (default: `false`)
* **commitMessage**: An optional commit message.
* **username**: The SVN username - leave empty if no authentication is required (default: `$project.svntools.username`)
* **password**: The SVN password - leave empty if no authentication is required (default: `$project.svntools.password`)
