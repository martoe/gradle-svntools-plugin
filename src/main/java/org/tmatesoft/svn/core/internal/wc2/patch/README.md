A patched version (line 705) of the original `svnkit/src/main/java/org/tmatesoft/svn/core/internal/wc2/patch/SvnPatchTarget.java` file to fix the `applyPatch()` method on Windows systems:

The `isUnderRoot()` method must use forward slashes for the `canonicalFullPath` var, otherwise the call to `SVNPathUtil.isAncestor()` will always return `false` (and then every file of the patch will be ignored). 
