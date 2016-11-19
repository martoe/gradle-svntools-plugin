## [SvnDelete](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnDelete.groovy) task

Allows scheduling of file paths delete within an SVN workspace (equivalent to svn delete).

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
ignoreErrors       | Continue the build if the specified paths conflict with the WC status (can't delete) | `false`

method             | Description 
------------------ | -----------------------
delete             | Files to be deleted by this task
