/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.ant;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;

/**
 * Container task to run JUnit/TestNG tasks with the GZoltar agent jar. Coverage will only be
 * applied if all of the following are true:
 * <ul>
 * <li>Exactly one sub task may be present</li>
 * <li>Task must be either JUnit or TestNG</li>
 * <li>Task must be using a forked VM (so VM args can be passed)</li>
 * </ul>
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's ant module for convenience.
 */
public class CoverageTask extends AbstractCoverageTask implements TaskContainer {

  private final List<JavaLikeTaskEnhancer> taskEnhancers = new ArrayList<JavaLikeTaskEnhancer>();

  private Task childTask;

  /**
   * Creates a new default coverage task
   */
  public CoverageTask() {
    super();
    this.taskEnhancers.add(new JavaLikeTaskEnhancer("java"));
    this.taskEnhancers.add(new JavaLikeTaskEnhancer("junit"));
    this.taskEnhancers.add(new TestNGTaskEnhancer("testng"));
  }

  /**
   * Add child task to this container and reconfigure it to run with coverage enabled
   */
  public void addTask(final Task task) {
    if (this.childTask != null) {
      throw new BuildException("Only one child task can be supplied to the code coverge task",
          getLocation());
    }

    this.childTask = task;

    final String subTaskTypeName = task.getTaskType();

    final JavaLikeTaskEnhancer enhancer = this.findEnhancerForTask(subTaskTypeName);
    if (enhancer == null) {
      throw new BuildException(
          String.format("%s is not a valid child of the code coverage task", subTaskTypeName),
          getLocation());
    }

    if (isEnabled()) {
      log(String.format("Enhancing %s with code coverage", this.childTask.getTaskName()));
      enhancer.enhanceTask(task);
    }

    task.maybeConfigure();
  }

  private JavaLikeTaskEnhancer findEnhancerForTask(final String taskName) {
    for (final JavaLikeTaskEnhancer enhancer : this.taskEnhancers) {
      if (enhancer.supportsTask(taskName)) {
        return enhancer;
      }
    }

    return null;
  }

  /**
   * Executes subtask and performs any required cleanup
   */
  @Override
  public void execute() throws BuildException {
    if (this.childTask == null) {
      throw new BuildException("A child task must be supplied for the coverage task",
          getLocation());
    }
    this.childTask.execute();
  }

  /**
   * Task enhancer for TestNG. TestNG task always run in a forked VM and has nested jvmargs elements
   */
  private class TestNGTaskEnhancer extends JavaLikeTaskEnhancer {

    public TestNGTaskEnhancer(final String supportedTaskName) {
      super(supportedTaskName);
    }

    @Override
    public void enhanceTask(final Task task) {
      this.addJvmArgs(task);
    }

  }

  /**
   * Basic task enhancer that can handle all 'java like' tasks. That is, tasks that have a top level
   * fork attribute and nested jvmargs elements
   */
  private class JavaLikeTaskEnhancer {

    private final String supportedTaskName;

    public JavaLikeTaskEnhancer(final String supportedTaskName) {
      this.supportedTaskName = supportedTaskName;
    }

    /**
     * @param taskname Task type to enhance
     * @return <code>true</code> if this enhancer is capable of enhancing the requested task type
     */
    public boolean supportsTask(final String taskname) {
      return taskname.equals(this.supportedTaskName);
    }

    /**
     * Attempt to enhance the supplied task with coverage information. This operation may fail if
     * the task is being executed in the current VM.
     * 
     * @param task Task instance to enhance (usually an {@link UnknownElement})
     */
    public void enhanceTask(final Task task) {
      final RuntimeConfigurable configurableWrapper = task.getRuntimeConfigurableWrapper();

      final String forkValue = getProject()
          .replaceProperties((String) configurableWrapper.getAttributeMap().get("fork"));

      if (!Project.toBoolean(forkValue)) {
        throw new BuildException("Code coverage can only be performed on a forked VM",
            getLocation());
      }

      this.addJvmArgs(task);
    }

    /**
     * Add JVM arguments to the supplied task.
     * 
     * @param task Task instance to enhance (usually an {@link UnknownElement})
     */
    public void addJvmArgs(final Task task) {
      final UnknownElement el = new UnknownElement("jvmarg");
      el.setTaskName("jvmarg");
      el.setQName("jvmarg");

      final RuntimeConfigurable runtimeConfigurableWrapper = el.getRuntimeConfigurableWrapper();
      runtimeConfigurableWrapper.setAttribute("value", getLaunchingArgument());

      task.getRuntimeConfigurableWrapper().addChild(runtimeConfigurableWrapper);

      ((UnknownElement) task).addChild(el);
    }
  }
}
