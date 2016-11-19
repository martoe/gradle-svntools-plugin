## [SvnDelete](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnDelete.groovy) task

Deletes files and directories (within an SVN working copy) and schedules them to be removed from version control.

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
delete             | Files and directories to be deleted by this task |
ignoreErrors       | Continue the build if the specified paths conflict with the WC status (can't delete) | `false`
username           | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password           | The SVN password - leave empty if no authentication is required | `$project.svntools.password`
