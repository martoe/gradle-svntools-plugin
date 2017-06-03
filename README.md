# gradle-svntools-plugin

A [Gradle](https://www.gradle.org) plugin (based on [SVNKit](http://svnkit.com/)) that provides various [Subversion](http://svnbook.red-bean.com/)-related tasks.

The svntools-plugin can interact with existing SVN workspaces as well as create new workspaces (by performing a svn-checkout). It can interact with any SVN working copy format; no additional SVN client is required.

Please report bugs and feature requests at the [Github issue page](https://github.com/martoe/gradle-svntools-plugin/issues).

## Version compatibility

Plugin version | Gradle version | Java version 
-------------- | -------------- | -------------
[up to 1.7](../gradle2/README.md) | 2.0 - 2.14     | 1.6 and above
2.0 and above  | 3.0 and above  | 1.7 and above

## Usage

* [Applying the plugin](docs/ApplyPlugin.md)
* [General Configuration](docs/GeneralConfig.md)
* [Examples](examples/)

## Available tasks

* [SvnAdd](docs/SvnAdd.md): schedules files within a working copy to be added to SVN
* [SvnApplyPatch](docs/SvnApplyPatch.md): applies a patch file
* [SvnBranch](docs/SvnBranch.md): creates an SVN branch
* [SvnCheckout](docs/SvnCheckout.md): creates a local working copy of an SVN repository
* [SvnCleanup](docs/SvnCleanup.md): cleans up a working copy
* [SvnCommit](docs/SvnCommit.md): commits modifications of a local working copy
* [SvnCreatePatch](docs/SvnCreatePatch.md): creates a patch file based on modifications of a local working copy
* [SvnDelete](docs/SvnDelete.md): schedules files within a working copy to be deleted from SVN
* [SvnExport](docs/SvnExport.md): exports parts of an SVN repository to a local directory
* [SvnInfo](docs/SvnInfo.md): information about a local working copy file
* [SvnRevert](docs/SvnRevert.md): reverts modifications of a local working copy
* [SvnTag](docs/SvnTag.md): creates an SVN tag
* [SvnUpdate](docs/SvnUpdate.md): updates a local working copy
* [SvnVersion](docs/SvnVersion.md): summarize the local revision(s) of a working copy.

[![Build Status](https://api.travis-ci.org/martoe/gradle-svntools-plugin.svg?branch=develop)](https://travis-ci.org/martoe/gradle-svntools-plugin)
[![Coverage Status](https://coveralls.io/repos/github/martoe/gradle-svntools-plugin/badge.svg?branch=develop)](https://coveralls.io/github/martoe/gradle-svntools-plugin?branch=develop)
