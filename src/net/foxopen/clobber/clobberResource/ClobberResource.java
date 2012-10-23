package net.foxopen.clobber.clobberResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.foxopen.clobber.ChangeListener;
import net.foxopen.clobber.ChangeNotifier;
import net.foxopen.clobber.clobRule.ClobRule;
import net.foxopen.clobber.clobRule.ClobRuleGenerator;
import net.foxopen.clobber.clobRule.ClobRuleProvider;
import net.foxopen.clobber.clobberResource.clobberConnection.ClobberConnection;
import net.foxopen.clobber.clobberResource.fileSystemLocation.FileSystemLocation;
import net.foxopen.clobber.exception.ClobbingFailedException;
import nu.xom.Element;

import org.apache.log4j.Logger;

/**
 * A represents file system locations to listen on and an action to take when
 * the resource changes.
 * 
 * @author aled
 * 
 */
public class ClobberResource implements ClobRuleProvider, ChangeNotifier {
  private ClobRule             clobRule;
  private ClobberConnection    clobAction;
  private FileSystemLocation   location;
  private boolean              isActive;
  private Date                 lastClobbed;
  private Logger               logger;
  private List<ChangeListener> projectChangeListeners;

  /**
   * @param location
   *          The location of the file
   * @param connection
   *          The instance of remote connection the clobber resource is using to
   *          update changed files
   * @param isActive
   *          Whether the resource is being listened on
   * @param lastClobbed
   *          When the file was last clobbed
   */
  public ClobberResource(FileSystemLocation location, ClobberConnection connection, boolean isActive, Date lastClobbed) {
    this.location = location;
    this.clobAction = connection;
    this.isActive = isActive;
    this.lastClobbed = lastClobbed;
    this.logger = Logger.getLogger(this.getClass());
    this.projectChangeListeners = new ArrayList<ChangeListener>();
  }

  public Date getLastClobbed() {
    return this.lastClobbed;
  }

  public Date getLastModified() {
    // TODO Auto-generated method stub
    return this.location.getLastModified();
  }

  public String getResourceConnectionDescription() {
    return this.clobAction.getConnectionName();
  }

  /**
   * Gets the path associated with this ClobberResource.
   * 
   * @return The path associated with this clobber resource.
   */
  public Path getResourceDirectory() {
    return this.location.getPath();
  }

  public String getResourceName() {
    return this.location.toString();
  }

  /**
   * @return Whether the clobber resource is active
   */
  public boolean isActive() {
    return this.isActive;
  }

  public boolean isLocalFileSystemLocationExtant() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isPreviousClobbedResourceDifferentFromCurrentRemoteResource(Path path) {
    Boolean different = false;

    try {
      different = this.clobAction.isDifferentToActionTarget(new FileReader(path.toFile()));
      return different;
    } catch (FileNotFoundException e) {
      this.logger.warn(e.getMessage());
      this.logger.warn("Could not determine whether local file is different from remote");
      return false;
    }
  }

  public boolean isResourceActive() {
    return this.isActive;
  }

  @Override
  public boolean isResourceReadOnly(Path path) {
    return !this.location.isWriteable(path);
  }

  /**
   * Checks whether an action needs to be taken and executes it if necessary.
   * 
   * @param eventPath
   *          The Path to check against the ClobberResource's Path
   */
  public void processEventPath(Path eventPath) {
    Logger l = Logger.getLogger(ClobberResource.class);
    if (this.location.matchesPath(eventPath) && this.isActive && this.clobRule.shouldClob(eventPath)) {
      l.info("Clobbing: " + eventPath.toString());
      try {
        this.clobAction.doAction(new FileInputStream(eventPath.toFile()), eventPath.getFileName().toString());
        this.lastClobbed = new Date();
      } catch (FileNotFoundException e) {
        // TODO think of something better, logging?
        e.printStackTrace();
      } catch (ClobbingFailedException e) {
        // TODO think of something better, logging?
        e.printStackTrace();
      }
    } else {
      l.debug("Not Clobbing");
    }
  }

  /**
   * @param isActive
   *          Whether the project should be active
   */
  public void setActive(boolean isActive) {
    Logger l = Logger.getLogger(ClobberResource.class);
    l.debug("Activity of " + this.getResourceName() + " is " + isActive);
    this.isActive = isActive;
    this.notifyListeners();
  }

  /**
   * Sets the clob rules using the ClobRuleGenerator supplied
   * 
   * @param clobRuleGenerator
   *          The clobRuleGenerator to use.
   */
  public void setClobRule(ClobRuleGenerator clobRuleGenerator) {
    this.clobRule = clobRuleGenerator.generateClobRule(this);
  }

  public ClobberConnection getConnection() {
    return this.clobAction;
  }

  public Element getFilesystemElement() {
    return this.location.getElementRepresentation();
  }

  @Override
  public void registerListener(ChangeListener projectToNotify) {
    this.projectChangeListeners.add(projectToNotify);

  }

  @Override
  public void removeListener(ChangeListener projectToNotify) {
    this.projectChangeListeners.remove(projectToNotify);

  }

  @Override
  public void notifyListeners() {
    for (ChangeListener listener : this.projectChangeListeners) {
      listener.changed();
    }

  }

}
