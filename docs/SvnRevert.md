## [SvnCommit](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnRevert.groovy) task

Reverts all local changes of the given files or directories (which must be part of an SVN working copy).
Items that are not under version control will be ignored.

### Configuration

Property      | Description | Default value
------------- | ----------- | -------------
revert        | A list of local workspace files or directories that should be reverted | `project.projectDir`
recursive     | Also revert items in subdirectories | `false`
username      | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password      | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

    apply plugin: "at.bxm.svntools"

    task revertSources(type: at.bxm.gradleplugins.svntools.tasks.SvnRevert) {
      revert "src/main/java", "src/main/resources"
      recursive = true
    }
