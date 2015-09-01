## SvnTag task (at.bxm.gradleplugins.svntools.tasks.SvnTag)

Creates an SVN tag based on a local SVN workspace.

### Configuration

* **workspaceDir**: Local workspace that should be tagged (default: `$project.projectDir`)
* **tagName**: Name of the SVN tag (required, no default)
* **replaceExisting**: If the tag already exists, delete it first (default: `false`)
* **commitMessage**: An optional commit message.
* **username**: The SVN username - leave empty if no authentication is required (default: `$project.svntools.username`)
* **password**: The SVN password - leave empty if no authentication is required (default: `$project.svntools.password`)

### Example

The `release` task creates an SVN tag using the current version number:

    apply plugin: "at.bxm.svntools"

    version = "1.0-SNAPSHOT"

    task svnTag(type: at.bxm.gradleplugins.svntools.tasks.SvnTag) {
      tagName = "v$project.version"
      commitMessage = "Release version $project.version"
    }

    task release(dependsOn: svnTag)
