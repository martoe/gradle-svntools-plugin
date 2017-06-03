## [SvnCleanup](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnCleanup.groovy) task

Recursively cleans up the working copy, removing locks and resuming unfinished operations.

### Configuration

Property           | Description | Default value
------------------ | ----------- | -------------
cleanup            | Directories to be recursively cleaned up by this task |
username           | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password           | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

    apply plugin: "at.bxm.svntools"

    task svnCleanup(type: at.bxm.gradleplugins.svntools.tasks.SvnCleanup) {
      cleanup "$project.projectDir/my-working-copy"
    }
