## [SvnAdd](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnAdd.groovy) task

Schedules files (within an SVN working copy) to be added to version control.

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
add                | Files and directories to be added by this task |
recursive          | Also add items in subdirectories | `false`
ignoreErrors       | Continue the build if the specified paths conflict with the WC status (can't add) | `false`
username           | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password           | The SVN password - leave empty if no authentication is required | `$project.svntools.password`


### Example

This Gradle script creates a `svn.properties` file that contains the SVN URL and revision of the buildfile, and adds it to the JAR artifact:

    apply plugin: "at.bxm.svntools"

    version = "1.0-SNAPSHOT"
    String previousVersion = "0.1"

    task createChangelog() {
      file("changelog_${project.version}.txt").text = "list of changes for this release..."
      
      file("changelog_${previousVersion}.txt")
    }

    task addChangelog(type: at.bxm.gradleplugins.svntools.tasks.SvnAdd, dependsOn: createChangelog) {
      add "changelog_${project.version}.txt"
    }
