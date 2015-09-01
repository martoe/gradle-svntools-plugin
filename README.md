# gradle-svntools-plugin

A [Gradle](https://www.gradle.org) plugin (based on [SVNKit](http://svnkit.com/)) that provides various [Subversion](http://svnbook.red-bean.com/)-related tasks.

Here is a very short build script that prints out the SVN revision:

    apply plugin: "at.bxm.svntools"
    task info << {
      println "Current revision is $svntools.info.revisionNumber"
    }

The svntools-plugin can interact with existing SVN workspaces as well as create new workspaces (by performing a svn-checkout). It can interact with any SVN working copy format; no additional SVN client is required.

Please report bugs and feature requests at the [Github issue page](https://github.com/martoe/gradle-svntools-plugin/issues).

## Use-case examples

* Add the SVN revision to the version number when publishing artifacts
* Create a tag as part of an automated release process
* Commit files that have been changed during the build process (e.g. bumped version numbers)

## Usage

* [Applying the plugin](docs/ApplyPlugin.md)
* [General Configuration](docs/GeneralConfig.md)
* [The 'SvnInfo' task](docs/SvnInfo.md)
* [The 'SvnCheckout' task](docs/SvnCheckout.md)
* [The 'SvnCommit' task](docs/SvnCommit.md)
* [The 'SvnBranch' task](docs/SvnBranch.md)
* [The 'SvnTag' task](docs/SvnTag.md)

[![Build Status](https://travis-ci.org/martoe/gradle-svntools-plugin.png)](https://travis-ci.org/martoe/gradle-svntools-plugin)
