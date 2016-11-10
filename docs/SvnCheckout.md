## [SvnCheckout](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnCheckout.groovy) task

Creates an SVN workspace by checking out an SVN URL to a local directory. Also supports updating an existing workspace.
If you just need the versioned files without SVN metadata, use the [SvnExport](SvnExport.md) task.

### Configuration

Property     | Description | Default value
------------ | ----------- | -------------
svnUrl       | The remote repository URL (required) |
workspaceDir | The target directory for checkout (required). If it doesn't exist it will be created. If it exists (and the `update` flag is not set) it must be empty |
revision     | The revision number to be checked out | `HEAD`
depth        | The checkout depth - either `empty`, `files`, `immediates`, or `infinity`. See [SvnDepth](../src/main/groovy/at/bxm/gradleplugins/svntools/api/SvnDepth.groovy) | `infinity`
username     | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password     | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
update       | If "workspaceDir" already contains checked-out data, update it instead of performing a fresh checkout | `false`

### Example

See [checkout examples](../examples/README.md#svn-checkout)
