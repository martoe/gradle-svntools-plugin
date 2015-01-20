package at.bxm.gradleplugins.svntools

import org.gradle.api.*

class SvnToolsPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create("svntools", SvnToolsPluginExtension, project)
    createSvnInfoTask project
    createSvnCommitTask project
    createSvnTagTask project
  }

  private static Task createSvnInfoTask(Project project) {
    addTask(project, "svnInfo", SvnInfo, "Provides information about the current SVN workspace")
  }

  private static Task createSvnCommitTask(Project project) {
    addTask(project, "svnCommit", SvnCommit, "Commits a list of files to SVN")
  }

  private static Task createSvnTagTask(Project project) {
    addTask(project, "svnTag", SvnTag, "Creates an SVN tag based on a local SVN workspace")
  }

  private static Task addTask(Project project, String name, Class<? extends DefaultTask> type, String description) {
    def task = project.task([(Task.TASK_TYPE): type], name)
    task.group = "svnTools"
    task.description = description
    return task
  }
}
