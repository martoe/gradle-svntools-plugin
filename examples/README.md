# Sample Gradle scripts

To use a local snapshot version instead of the latest release, execute `gradlew clean publishToMavenLocal` first


## SVN Checkout

Checking out the gradle-svntools-plugin sources:

    gradlew -b examples/checkout/build.gradle checkoutSvntools

[Sources](checkout/)

## SVN Version

Print some status information for a checked-out workspace:

    gradlew -b examples/version/build.gradle printVersion

[Sources](version/)

## SVN Patches

Creating a patch file from a modified workspace:

    gradlew -b examples/patch/build.gradle createPatch

Applying a patch file to a workspace:

    gradlew -b examples/patch/build.gradle applyPatch

[Sources](patch/)
