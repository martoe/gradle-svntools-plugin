Release process steps, to be automated...

1. checkout "master" and merge "develop"
```
git checkout master
git merge --no-ff develop
```

1. remove "-SNAPSHOT" and set release-notes in gradle.properties

1. `gradlew clean publishPluginToBintray`

1. commit and push gradle.properties
```
git commit -am "Release 0.0"
git push
```

1. [create Github release](https://github.com/martoe/gradle-svntools-plugin/releases/new)
    * Tag version: "v0.0" for feature releases, "v0.0.0" for bugfix releases, @master
    * Release title: same as "Tag version"
    * Description: from `gradle.properties`

1. checkout "develop" and rebase against "master"
```
git checkout develop
git rebase master
```

1. bump version and add "-SNAPSHOT" in gradle.properties (and reset release-notes)

1. commit and push gradle.properties
```
git commit -am "Post-release"
git push
```
