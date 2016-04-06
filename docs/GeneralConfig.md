## General Configuration

The `svntools` block (implemented by [SvnToolsPluginExtension](../src/main/groovy/at/bxm/gradleplugins/svntools/SvnToolsPluginExtension.groovy)) can be used to

* specify default values for some configuration properties:
    * **username**: The SVN username - leave empty if no authentication is required
    * **password**: The SVN password - leave empty if no authentication is required
* adjust proxy server settings (see below)
* access information about the current SVN workspace root (i.e. the project's root directory), wrapped by an [SvnData](../src/main/groovy/at/bxm/gradleplugins/svntools/api/SvnData.groovy) object:
    * **info.revisionNumber** The SVN revision number
    * **info.url** The complete SVN URL of the checked-out project
    * **info.repositoryRootUrl** The root URL of the SVN repository
    * **info.name** Either "trunk", the name of the current branch, or the name of the current tag
    * **info.trunk** "true" if the SVN URL refers to a trunk
    * **info.branch** "true" if the SVN URL refers to a branch
    * **info.tag** "true" if the SVN URL refers to a tag
* summarize the local revision(s) of a working copy, wrapped by an [SvnVersionData](../src/main/groovy/at/bxm/gradleplugins/svntools/api/SvnVersionData.groovy) object:
    * **version** [svnversion](http://svnbook.red-bean.com/en/1.7/svn.ref.svnversion.re.html) output
    * **version.mixedRevision** "true" if the working copy contains mixed revisions
    * **version.minRevisionNumber** The smallest SVN revision within the working copy
    * **version.maxRevisionNumber** The greatest SVN revision within the working copy
    * **version.modified** "true" if the working copy contains local modifications
    * **version.sparse** "true" if the working copy is sparsely populated (i.e. "depth" is not "infinity")
    * **version.switched** "true" if the parts of the working copy have been switched

Note: The `svntools.info` and `svntools.version` objects assume that the current Gradle project has been checked out from SVN. To retrieve information about other SVN files or workspaces, use the [SvnInfo](SvnInfo.md) resp. [SvnVersion](SvnVersion.md) tasks.

### Example

    apply plugin: "at.bxm.svntools"

    svntools {
      username = "john"
      password = "secret"
    }

    task info << {
      println "Current revision is $svntools.info.revisionNumber"
    }



## Using a Proxy Server

If a proxy server is needed for connecting to the remote SVN repository, it can be configured in one of the following ways (ordered by precedence):

### Specify the proxy settings in `build.gradle`

    svntools {
        proxy.host = "[hostname]"
        proxy.port = [portnumber]
        proxy.username = "[username]"
        proxy.password = "[password]"
    }

This way, the proxy settings are only applied to the svn-tools-plugin and are ignored by Gradle.

### Specify the proxy settings at the command line

    gradlew -Dhttp.proxyHost=[hostname] \
            -Dhttp.proxyPort=[portnumber] \
            -Dhttp.proxyUser=[username] \
            -Dhttp.proxyPassword=[password]
            [taskname]

Now all tasks of the current Gradle execution are using the proxy server (e.g. dependencies are downloaded through the proxy server)

### Specify the proxy settings in `~/gradle.properties`

    systemProp.http.proxyHost = [hostname]
    systemProp.http.proxyPort = [portnumber]
    systemProp.http.proxyUser = [username]
    systemProp.http.proxyPassword = [password]

Every Gradle execution will use the proxy server.
