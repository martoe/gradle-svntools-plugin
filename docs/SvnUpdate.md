## SvnUpdate task (at.bxm.gradleplugins.svntools.tasks.SvnUpdate)

Updates an SVN workspace.

### Configuration

Property      | Description | Default value
------------- | ----------- | -------------
workspaceDir  | The local workspace that should be updated. | `$project.projectDir`
revision      | The revision number to be checked out | `HEAD`
username      | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password      | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
