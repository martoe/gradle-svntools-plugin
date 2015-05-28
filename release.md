Release process steps, to be automated...

1. checkout "master" and merge "develop"
1. remove "-SNAPSHOT" in build.gradle
1. update version in README.md
1. _set releasenotes in build.gradle?_
1. `gradle clean publishPluginToBintray`
1. commit and push build.gradle, README.md
1. create Github release _or_ create and push tag
1. checkout "develop" and rebase against "master"
1. bump version and add "-SNAPSHOT" in build.gradle
1. commit and push build.gradle
