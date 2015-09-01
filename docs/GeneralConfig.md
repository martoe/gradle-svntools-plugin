## General Configuration

The `svntools` block (implemented by `at.bxm.gradleplugins.svntools.SvnToolsPluginExtension`) can be used to

* specify default values for some configuration properties:
    * **username**: The SVN username - leave empty if no authentication is required
    * **password**: The SVN password - leave empty if no authentication is required
* access information about the current SVN workspace (i.e. the project's root directory), wrapped by an `at.bxm.gradleplugins.svntools.api.SvnData` object:
    * **info.revisionNumber** The SVN revision number
    * **info.url** The complete SVN URL of the checked-out project
    * **info.repositoryRootUrl** The root URL of the SVN repository
    * **info.name** Either "trunk", the name of the current branch, or the name of the current tag
    * **info.trunk** "true" if the SVN URL refers to a trunk
    * **info.branch** "true" if the SVN URL refers to a branch
    * **info.tag** "true" if the SVN URL refers to a tag

Note: The `svntools.info` object assumes that the current Gradle project has been checked out from SVN. To retrieve information about other SVN files or workspaces, use the "SvnInfo" task below.

### Example

    apply plugin: "at.bxm.svntools"

    svntools {
      username = "john"
      password = "secret"
    }

    task info << {
      println "Current revision is $svntools.info.revisionNumber"
    }
