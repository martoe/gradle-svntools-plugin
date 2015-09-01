## SvnCheckout task (at.bxm.gradleplugins.svntools.tasks.SvnCheckout)

Creates an SVN workspace by checking out an SVN URL to a local directory.

### Configuration

* **svnUrl**: The remote repository URL (required)
* **workspaceDir**: The target directory for checkout (required). If it doesn't exist it will be created. If it exists it must be empty.
* **revision**: The revision number to be checked out (optional, defaults to HEAD)
* **username**: The SVN username - leave empty if no authentication is required (default: `$project.svntools.username`)
* **password**: The SVN password - leave empty if no authentication is required (default: `$project.svntools.password`)

### Example

This Gradle script performs a checkout from a local SVN repository into `build/workspace`:

    apply plugin: "at.bxm.svntools"

    task checkout(type: at.bxm.gradleplugins.svntools.tasks.SvnCheckout) {
      svnUrl = "file:///home/user/svn/repo/myproject/trunk"
      workspaceDir = "$project.buildDir/workspace"
    }
