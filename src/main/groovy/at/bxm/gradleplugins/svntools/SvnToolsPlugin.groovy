package at.bxm.gradleplugins.svntools

import org.gradle.api.*

class SvnToolsPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    createSvnInfoTask project
  }

  private Task createSvnInfoTask(Project project) {
    addTask(project, "svnInfo", SvnInfo, "Provides information about the current SVN workspace")
  }

  private Task addTask(Project project, String name, Class<? extends DefaultTask> type, String description) {
    def task = project.task([(Task.TASK_TYPE): type], name)
    task.group = "svnTools"
    task.description = description
    return task
  }
}
