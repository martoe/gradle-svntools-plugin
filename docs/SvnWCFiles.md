## [SvnWCFiles](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnWCFiles.groovy) task

Allows scheduling of file paths add / delete within an SVN workspace (equivalent to svn add / svn delete).

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
sourcePath         | Source path for checkout dir | `$project.projectDir`
ignoreErrors       | Continue the build if the specified paths conflict with the WC status (can't dadd/delete) | `false`

method             | Description 
------------------ | -----------------------
add                | Pass include / exclude pattern in a Closure similar to project.fileTree
delete             | Pass include / exclude pattern in a Closure similar to project.fileTree

### Example

This Gradle script creates a `svn.properties` file that contains the SVN URL and revision of the buildfile, and adds it to the JAR artifact:

    apply plugin: "at.bxm.svntools"

    version = "1.0-SNAPSHOT"
    String previousVersion = "0.1"

    task createChangelog() {
      file("changelog_${project.version}.txt").text = "list of changes for this release..."
      
      file("changelog_${previousVersion}.txt")
    }

    task replaceChangelog(type: at.bxm.gradleplugins.svntools.tasks.SvnWCFiles, dependsOn: createChangelog) {
      add {
        include "changelog_${project.version}.txt"  // Specify include / exclude patterns relative to sourcePath
      }
      delete {
        include "changelog_${previousVersion}.txt" // Specify include / exclude patterns relative to sourcePath
      }
    }

