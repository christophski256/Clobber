package net.foxopen.clobber;

import java.util.ArrayList;
import java.util.List;

import net.foxopen.clobber.params.YesNoCancelType;
import net.foxopen.clobber.prompts.Prompter;
import nu.xom.Element;
import nu.xom.Text;

/**
 * An instance of a Clobber Model. Holds a list of ClobberProjects.
 * 
 * @author Aled Lewis
 * 
 */
public class ClobberInstance {
  private List<ClobberProject> projects;

  ClobberInstance() {
    this.projects = new ArrayList<ClobberProject>();
  }

  ClobberInstance(List<ClobberProject> projects) {
    this();
    this.projects = projects;

  }

  /**
   * Adds a clobber project to this clobber instance. This then starts the
   * project listening for filesystem changes
   * 
   * @param project
   *          The project to add
   */
  public void addProjectToInstance(ClobberProject project) {
    this.projects.add(project);
    project.startListening();
  }

  /**
   * Returns all the projects in this instance.
   * 
   * @return The projects in this instance.
   */
  public List<ClobberProject> getProjects() {
    return this.projects;
  }

  /**
   * Removes the supplied clobber project from the clobber instance. This stops
   * the project listening to filesystem changes
   * 
   * @param project
   *          The project to remove
   */
  public void removeProjectFromInstance(ClobberProject project) {
    project.stopListening();
    this.projects.remove(project);
  }

  /**
   * Converts the project to a document
   * 
   * @return The document representation of the instance
   */
  public Element getOpenProjectsElement() {

    Element openProjectList = new Element("OPEN_PROJECT_LIST");
    for (ClobberProject cp : this.projects) {
      Element projectFileLocation = new Element("PROJECT_FILE_LOCATION");
      projectFileLocation.appendChild(new Text(cp.getFileLocation()));
      Element openProject = new Element("OPEN_PROJECT");
      openProject.appendChild(projectFileLocation);
      openProjectList.appendChild(openProject);
    }
    return openProjectList;
  }

  /**
   * Causes the project to start listening for changed files
   */
  public void startAllProjectsListening() {
    for (ClobberProject p : this.projects) {
      p.startListening();
    }
  }

  /**
   * Stops all the projects from listening for file changes.
   */
  public void stopAllProjectsListening() {
    for (ClobberProject p : this.projects) {
      p.stopListening();
    }

  }

  public boolean doCloseOperations() {

    // only close if no cancels happen on changed projects
    boolean shouldClose = true;

    for (ClobberProject cp : this.projects) {
      if (cp.hasChanged()) {
        YesNoCancelType type =new YesNoCancelType(Prompter.getPrompter().getOption(YesNoCancelType.getOrderedOptions(), "Do you want to save project"+ cp.getName()));
        if (type.getValue() == YesNoCancelType.YES) {
          cp.save();
        } else if (type.getValue() == YesNoCancelType.CANCEL) {
          shouldClose = false;
        }
      }
    }

    return shouldClose;
  }

}
