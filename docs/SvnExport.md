## [SvnExport](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnExport.groovy) task

Exports parts of an SVN repository to a local directory.
Different from the [SvnCheckout](SvnCheckout.md) task, the target directory will receive the versioned files only, but no SVN metadata.

### Configuration

Property     | Description | Default value
------------ | ----------- | -------------
svnUrl       | The remote repository URL (required) |
targetDir    | The target directory for export (required). If it doesn't exist it will be created. If it exists it must be empty |
revision     | The revision number to be exported | `HEAD`
username     | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password     | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

This Gradle script performs an export from a local SVN repository into `build/export`:

    apply plugin: "at.bxm.svntools"

    task export(type: at.bxm.gradleplugins.svntools.tasks.SvnExport) {
      svnUrl = "file:///home/user/svn/repo/myproject/trunk"
      targetDir = "$project.buildDir/export"
    }
