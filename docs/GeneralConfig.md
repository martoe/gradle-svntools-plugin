## General Configuration

The `svntools` block (implemented by [SvnToolsPluginExtension](../src/main/groovy/at/bxm/gradleplugins/svntools/SvnToolsPluginExtension.groovy)) can be used to

* specify default values for some configuration properties:
    * **username**: The SVN username - leave empty if no authentication is required
    * **password**: The SVN password - leave empty if no authentication is required
* adjust proxy server settings (see below)
* access information about the current SVN workspace (i.e. the project's root directory), wrapped by an [SvnData](../src/main/groovy/at/bxm/gradleplugins/svntools/api/SvnData.groovy) object:
    * **info.revisionNumber** The SVN revision number
    * **info.url** The complete SVN URL of the checked-out project
    * **info.repositoryRootUrl** The root URL of the SVN repository
    * **info.name** Either "trunk", the name of the current branch, or the name of the current tag
    * **info.trunk** "true" if the SVN URL refers to a trunk
    * **info.branch** "true" if the SVN URL refers to a branch
    * **info.tag** "true" if the SVN URL refers to a tag

Note: The `svntools.info` object assumes that the current Gradle project has been checked out from SVN. To retrieve information about other SVN files or workspaces, use the [SvnInfo task](SvnInfo.md).

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

### Specify the proxy settings in `~/gradle.properties`

    systemProp.http.proxyHost = [hostname]
    systemProp.http.proxyPort = [portnumber]
    systemProp.http.proxyUser = [username]
    systemProp.http.proxyPassword = [password]
