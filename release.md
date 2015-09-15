Release process steps, to be automated...

1. checkout "master" and merge "develop"
1. remove "-SNAPSHOT" in gradle.properties
1. set release-notes in gradle.properties
1. `gradlew clean publishPluginToBintray`
1. commit and push gradle.properties
1. create Github release _or_ create and push tag
1. checkout "develop" and rebase against "master"
1. bump version and add "-SNAPSHOT" in gradle.properties (and reset release-notes)
1. commit and push gradle.properties
