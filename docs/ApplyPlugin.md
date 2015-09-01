## Applying the plugin

(see also the [Gradle plugin portal](https://plugins.gradle.org/plugin/at.bxm.svntools) page)

### Using the [Gradle plugins DSL](https://www.gradle.org/docs/current/userguide/plugins.html#sec:plugins_block) (Gradle 2.1 and above)

    plugins {
      id "at.bxm.svntools" version "latest.release"
    }

### Using an [external dependency](https://www.gradle.org/docs/current/userguide/organizing_build_logic.html#sec:external_dependencies)

    buildscript {
      repositories {
        jcenter()
      }
      dependencies {
        classpath "at.bxm.gradleplugins:gradle-svntools-plugin:latest.release"
      }
    }
    apply plugin: "at.bxm.svntools"
