## SvnCommit task (at.bxm.gradleplugins.svntools.tasks.SvnCommit)

Commits a list of files (and directories). All files and directories must be part of the same SVN workspace.

### Configuration

* **source**: A list of files and directories that should be committed.
              If these are not under version control already, they will be added first.
              If this list is empty of the files contain no modifications, no commit will be executed.
* **commitMessage**: An optional commit message.
* **username**: The SVN username - leave empty if no authentication is required (default: `$project.svntools.username`)
* **password**: The SVN password - leave empty if no authentication is required (default: `$project.svntools.password`)

### Example

This Gradle script commits a changelog file to SVN:

    apply plugin: "at.bxm.svntools"

    version = "1.0-SNAPSHOT"

    task createChangelog() {
      project.ext.changelog = file("changelog_${project.version}.txt")
      project.ext.changelog.text = "list of changes for this release..."
    }

    task commitChangelog(type: at.bxm.gradleplugins.svntools.tasks.SvnCommit, dependsOn: createChangelog) {
      source << project.ext.changelog
      commitMessage = "Changelog added"
    }
