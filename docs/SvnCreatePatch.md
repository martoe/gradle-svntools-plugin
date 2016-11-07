## [SvnCreatePatch](../src/main/groovy/at/bxm/gradleplugins/svntools/tasks/SvnCreatePatch.groovy) task

Creates a patch file that contains all modifications of a local workspace directory (including subdirectories).

### Configuration

Property      | Description | Default value
------------- | ----------- | -------------
source        | Local workspace files and directories with modifications that shall be saved to a patch file | `$project.projectDir`
patchFile     | The name of the target patch file, relative to the project directory (required). If it exists it will be overwritten |
username      | The SVN username - leave empty if no authentication is required | `$project.svntools.username`
password      | The SVN password - leave empty if no authentication is required | `$project.svntools.password`

### Example

    apply plugin: "at.bxm.svntools"

    task createPatch(type: at.bxm.gradleplugins.svntools.tasks.SvnCreatePatch) {
      source "src/main/java", "src/main/resources"
      patchFile = "target/myModifications.patch"
    }
