## [SvnAdd](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnAdd.groovy) task

Allows scheduling of file paths add within an SVN workspace (equivalent to svn add).

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
ignoreErrors       | Continue the build if the specified paths conflict with the WC status (can't add) | `false`

method             | Description 
------------------ | -----------------------
add                | Files to be added by this task

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
