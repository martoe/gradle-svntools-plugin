# Sample Gradle scripts

To use a local snapshot version instead of the latest release, execute `gradlew clean publishToMavenLocal` first

## SVN Checkout

Checking out the SVNKit sources:

    gradlew -b examples/checkout/build.gradle checkoutSvnkit

## SVN Patches

Creating a patch file from a modified workspace:

    gradlew -b examples/patch/build.gradle createPatch

Applying a patch file to a workspace:

    gradlew -b examples/patch/build.gradle applyPatch
