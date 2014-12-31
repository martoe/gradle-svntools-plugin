# gradle-svntools-plugin

A [Gradle](https://www.gradle.org) plugin (based on [SVNKit](http://svnkit.com/)) that provides various Subversion-related tasks.
[![Build Status](https://travis-ci.org/martoe/gradle-svntools-plugin.png)](https://travis-ci.org/martoe/gradle-svntools-plugin)

Binaries are hosted at [Bintray](https://bintray.com/martoe/gradle-plugins/gradle-svntools-plugin/)
and are available at the [jcenter Maven repo](https://bintray.com/bintray/jcenter/)

## Use-case examples

* Add the SVN revision to the version number when publishing artifacts
* Create a branch and/or tag as part of an automated release process
* Commit files that have been changed during the build process (e.g. bumped version numbers)

## Applying the plugin

(see also the [Gradle plugin portal](https://plugins.gradle.org/plugin/at.bxm.svntools/) page)

### Using the [Gradle plugins DSL](https://www.gradle.org/docs/current/userguide/plugins.html#sec:plugins_block) (Gradle 2.1 and above)

    plugins {
      id "at.bxm.svntools" version "0.4"
    }

### Using an [external dependency](https://www.gradle.org/docs/current/userguide/organizing_build_logic.html#sec:external_dependencies)

    buildscript {
      repositories {
        jcenter()
      }
      dependencies {
        classpath "at.bxm.gradleplugins:gradle-svntools-plugin:0.4"
      }
    }
    apply plugin: "at.bxm.svntools"


# Tasks

## svnInfo (at.bxm.gradleplugins.svntools.SvnInfo)

Creates a `at.bxm.gradleplugins.svntools.SvnData` object that contains information about the current SVN workspace.
The object is added as an "extra property" to the Gradle project and may be accessed with `$project.svnData`.

### Configuration

* **sourcePath**: Source path for reading the SVN metadata (default: `$project.projectDir`)
* **targetPropertyName**: The name of the project extra property that will receive the resulting SvnData object (default: `svnData`)
* **ignoreErrors**: Continue the build if the specified path doesn't contain SVN data (default: `false`)
* **username**: The SVN username - leave empty if no authentication is required (default: `$project.svntools.username`)
* **password**: The SVN password - leave empty if no authentication is required (default: `project.svntools.password`)

### Example

This Gradle script creates a `svn.properties` file that contains the SVN URL and revision, and adds it to the JAR artifact:

    plugins {
      id "java"
      id "at.bxm.svntools" version "0.4"
    }

    task svnStatus(dependsOn: svnInfo) << {
      def props = new Properties()
      props.setProperty("url", project.svnData.url)
      props.setProperty("revision", project.svnData.revisionNumber as String)
      file("svn.properties").withWriter { props.store(it, null) }
    }

    jar {
      dependsOn svnStatus
      from(projectDir, { include "svn.properties" })
    }

## svnCommit

under development

## svnTag

under development
