package at.bxm.gradleplugins.svntools

import at.bxm.gradleplugins.svntools.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class SvnToolsPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create("svntools", SvnToolsPluginExtension, project)
    project.extensions.extraProperties.set("SvnAdd", SvnAdd.class)
    project.extensions.extraProperties.set("SvnApplyPatch", SvnApplyPatch.class)
    project.extensions.extraProperties.set("SvnBranch", SvnBranch.class)
    project.extensions.extraProperties.set("SvnCheckout", SvnCheckout.class)
    project.extensions.extraProperties.set("SvnCleanup", SvnCleanup.class)
    project.extensions.extraProperties.set("SvnCommit", SvnCommit.class)
    project.extensions.extraProperties.set("SvnCreatePatch", SvnCreatePatch.class)
    project.extensions.extraProperties.set("SvnDelete", SvnDelete.class)
    project.extensions.extraProperties.set("SvnExport", SvnExport.class)
    project.extensions.extraProperties.set("SvnInfo", SvnInfo.class)
    project.extensions.extraProperties.set("SvnRevert", SvnRevert.class)
    project.extensions.extraProperties.set("SvnTag", SvnTag.class)
    project.extensions.extraProperties.set("SvnUpdate", SvnUpdate.class)
    project.extensions.extraProperties.set("SvnVersion", SvnVersion.class)
  }
}
