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

* Add the SVN revision to the MANIFEST file when publishing artifacts
* Create a tag as part of an automated release process
* Commit files that have been changed during the build process (e.g. bumped version numbers)

## Usage

* [Applying the plugin](docs/ApplyPlugin.md)
* [General Configuration](docs/GeneralConfig.md)
* [SvnInfo](docs/SvnInfo.md): information about a workspace file
* [SvnVersion](docs/SvnVersion.md): summarize the local revision(s) of a working copy.
* [SvnCheckout](docs/SvnCheckout.md): creates a local workspace of an SVN repository
* [SvnUpdate](docs/SvnUpdate.md): updates an SVN workspace
* [SvnCommit](docs/SvnCommit.md): commits workspace modifications
* [SvnRevert](docs/SvnRevert.md): reverts workspace modifications
* [SvnCreatePatch](docs/SvnCreatePatch.md): creates a patch file based on workspace modifications
* [SvnApplyPatch](docs/SvnApplyPatch.md): applies a patch file
* [SvnBranch](docs/SvnBranch.md): creates an SVN branch
* [SvnTag](docs/SvnTag.md): creates an SVN tag
* [SvnExport](docs/SvnExport.md): exports parts of an SVN repository to a local directory

[![Build Status](https://travis-ci.org/martoe/gradle-svntools-plugin.png)](https://travis-ci.org/martoe/gradle-svntools-plugin)
