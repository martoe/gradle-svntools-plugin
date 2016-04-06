## [SvnVersion](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnVersion.groovy) task

Creates a [SvnVersionData](../src/main/groovy/at/bxm/gradleplugins/svntools/api/SvnVersionData.groovy) object (see [General Configuration](GeneralConfig.md)) that contains information about an SVN working copy (similar to the [svnversion](http://svnbook.red-bean.com/en/1.7/svn.ref.svnversion.re.html) command).
The object is added as an "extra property" to the Gradle project and may be accessed with `$project.svnVersion`.

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
sourcePath         | Local SVN working copy directory | `$project.projectDir`
targetPropertyName | The name of the project extra property that will receive the resulting SvnVersionData object | `svnVersion`
ignoreErrors       | Continue the build if the specified path is no SVN working copy | `false`
username           | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password           | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

see [examples/SvnVersion/build.gradle](../examples/SvnVersion/build.gradle)
