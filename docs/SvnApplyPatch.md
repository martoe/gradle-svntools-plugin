## [SvnApplyPatch](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnApplyPatch.groovy) task

Applies a patch file onto a local workspace directory (without committing the changes).

### Configuration

Property      | Description | Default value
------------- | ----------- | -------------
patchFile     | The name of the patch file, relative to the project directory (required) |
dir           | The base directory to apply the patch (must be part of a local SVN workspace) | `$project.projectDir`
username      | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password      | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

    apply plugin: "at.bxm.svntools"

    task applyPatch(type: at.bxm.gradleplugins.svntools.tasks.SvnApplyPatch) {
      patchFile = "target/myModifications.patch"
      dir = "src/main/java"
    }
