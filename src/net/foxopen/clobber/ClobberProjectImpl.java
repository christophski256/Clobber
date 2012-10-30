package net.foxopen.clobber;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.foxopen.clobber.clobRule.ClobRuleGenerator;
import net.foxopen.clobber.clobberResource.ClobberResource;
import net.foxopen.clobber.clobberResource.ResourceSerialiser;
import net.foxopen.clobber.clobberResource.clobberConnection.AddResourceDialog;
import net.foxopen.clobber.clobberResource.clobberConnection.ClobberConnection;
import net.foxopen.clobber.clobberResource.factory.ClobberResourceGenerator;
import net.foxopen.clobber.exception.ClobberProjectInitialisationException;
import net.foxopen.clobber.params.ClobRuleParams;
import net.foxopen.clobber.params.PromptYesNoType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;

import org.apache.log4j.Logger;

/**
 * A project of ClobberResources. This has the file system listener.
 * 
 * @author aled
 * 
 */
public class ClobberProjectImpl implements Runnable, ChangeListener, ClobberProject {

  /**
   * Creates a clobber project
   * 
   * @param filePath
   *          The file path of the clobber project config XML
   * @return The new clobber project
   * @throws ClobberProjectInitialisationException
   *           When there has been an error creating a clobber project
   */
  public static ClobberProjectImpl createClobberProjectFromFile(String filePath) throws ClobberProjectInitialisationException {
    Builder b = new Builder();
    Document projectDocument;
    try {
      projectDocument = b.build(new File(filePath));
    } catch (ParsingException | IOException e) {
      throw new ClobberProjectInitialisationException(e);
    }

    List<ClobberResource> clobberResources = new ArrayList<ClobberResource>();

    Nodes clobberTypes = projectDocument.query("/CLOBBER_PROJECT/CLOBBER_TYPES/*");
    for (int i = 0; i < clobberTypes.size(); i++) {
      clobberResources.addAll(ClobberResourceGenerator.createClobberResources((Element) clobberTypes.get(i)));
    }

    ClobRuleParams crp = new ClobRuleParams();
    Nodes params = projectDocument.query("/CLOBBER_PROJECT/CLOB_RULE_LIST/CLOB_RULE");
    for (int i = 0; i < params.size(); i++) {
      Node param = params.get(i);
      crp.addParam(param.query("RULE_NAME/text()").get(0).getValue(), new PromptYesNoType(param.query("VALUE/text()").get(0).getValue()));
    }

    ClobRuleGenerator crg = new ClobRuleGenerator(crp);
    ClobberProjectImpl cp;
    try {
      cp = new ClobberProjectImpl(clobberResources, crg, filePath);
    } catch (IOException e) {
      throw new ClobberProjectInitialisationException(e);
    }
    return cp;

  }

  /**
   * Save the project to disk as an xml representation.
   * 
   * @param project
   *          The clobber project to save.
   */
  public static void saveProjectToFile(ClobberProjectImpl project) {

  }

  private List<ClobberResource> clobberResources;
  private WatchService          watcher;
  private volatile boolean      isListening;
  private volatile boolean      shouldRun;
  private volatile boolean      hasChanged;
  private Logger                logger;

  private ClobRuleGenerator     clobRuleGenerator;

  private File                  fileLocation;

  private ClobberProjectImpl(List<ClobberResource> clobberResources, ClobRuleGenerator clobRuleGenerator, String fileLocation) throws IOException {
    this.logger = Logger.getLogger(this.getClass());
    this.isListening = false;
    this.fileLocation = new File(fileLocation);
    this.watcher = FileSystems.getDefault().newWatchService();
    this.clobberResources = clobberResources;
    this.clobRuleGenerator = clobRuleGenerator;
    this.registerResourcesWithService();
    this.setResourceClobRules();
    this.hasChanged = false;
    for (ClobberResource clobberResource : clobberResources) {
      clobberResource.registerListener(this);
    }
  }

  /**
   * Adds a clobberResource to the project
   * 
   * @param cr
   *          The clobber resource to add to this project
   */
  public void addClobberResource(ClobberResource cr) {
    this.clobberResources.add(cr);
  }

  /**
   * Returns the filename of the XML document defining the project
   * 
   * @return The file where the project is defined
   */
  public String getFileLocation() {
    return this.fileLocation.getAbsolutePath();
  }

  /**
   * Get the name of the project
   * 
   * @return The name of the project.
   */
  public String getName() {
    String star = this.hasChanged ? "*" : "";
    return star + this.fileLocation.getName();
  }

  /**
   * Returns the list of clobber resources in the project
   * 
   * @return The list of Clobber Resources
   */
  public List<ClobberResource> getResources() {

    return this.clobberResources;
  }

  /**
   * returns whether the project is listening for file actions.
   * 
   * @return whether the project is listening for file actions.
   */
  public boolean isListening() {
    return this.isListening;
  }

  /**
   * Registers the clobberResources with a WatchService to listen for
   * ENTRY_MODIFY events.
   * 
   * @throws IOException
   */
  private void registerResourcesWithService() throws IOException {
    for (ClobberResource r : this.clobberResources) {
      // TODO hard-coding alert!!!
      r.getResourceDirectory().register(this.watcher, ENTRY_MODIFY);
    }
  }

  /**
   * Run thread execution for a file listener. Waits for fileSystem events and
   * sends the file system path of the resultant event to the clobber resources.
   */
  @Override
  public void run() {

    while (this.shouldRun) {
      this.isListening = true;

      try {
        WatchKey key = this.watcher.take();
        List<WatchEvent<?>> polledEvents = key.pollEvents();
        for (WatchEvent<?> event : polledEvents) {

          if (event.kind() == OVERFLOW) {
            continue;
          }
          // the events path forgets where the orignal path came from
          @SuppressWarnings("unchecked")
          Path eventPath = ((Path) key.watchable()).resolve(((WatchEvent<Path>) event).context());

          for (ClobberResource cr : this.clobberResources) {
            cr.processEventPath(eventPath);
          }
        }
        key.reset();
      } catch (InterruptedException e) {
        // TODO not really sure what to do here
        e.printStackTrace();
      }
    }
    this.isListening = false;
  }

  /**
   * Set the clob rules for each of the project's resources.
   */
  public void setResourceClobRules() {
    for (ClobberResource r : this.clobberResources) {
      r.setClobRule(this.clobRuleGenerator);
    }

  }

  /**
   * Starts the project's listening thread. Should only be run if the thread is
   * not running.
   */
  public void startListening() {

    this.shouldRun = true;
    Thread thread = new Thread(this);
    thread.start();
  }

  /**
   * Stops the project's listening thread. Should only be used if the thread is
   * running.
   */
  public void stopListening() {
    this.shouldRun = false;
  }

  private Document createDocumentForProject() {
    this.logger.debug("Generating document for project " + this.getName());

    Element root = new Element("CLOBBER_PROJECT");
    Element clobberTypes = new Element("CLOBBER_TYPES");
    root.appendChild(clobberTypes);
    Map<Class<? extends ClobberConnection>, List<ClobberResource>> connectionTypeResourceMap = new HashMap<Class<? extends ClobberConnection>, List<ClobberResource>>();

    // create the map of types to connections
    for (ClobberResource cr : this.clobberResources) {
      ClobberConnection cc = cr.getConnection();
      List<ClobberResource> resources = connectionTypeResourceMap.get(cc.getClass());
      if (resources == null) {
        resources = new ArrayList<ClobberResource>();
        connectionTypeResourceMap.put(cc.getClass(), resources);
      }
      resources.add(cr);
    }

    for (Map.Entry<Class<? extends ClobberConnection>, List<ClobberResource>> connectionEntry : connectionTypeResourceMap.entrySet()) {
      ResourceSerialiser rs = ClobberConnection.getSerialiser(connectionEntry.getKey());
      clobberTypes.appendChild(rs.getElementRepresentation(connectionEntry.getValue()));
    }

    this.logger.debug("Creating clob rule metadata");
    Element clobRulesElement = this.clobRuleGenerator.getClobRulesElement();
    root.appendChild(clobRulesElement);

    Document projectDoc = new Document(root);
    this.logger.debug("XML for Project " + this.getName() + " is " + projectDoc.toXML());
    return projectDoc;
  }

  @Override
  public void changed() {
    this.logger.debug("Project: " + this.getName() + " has changed.");
    this.hasChanged = true;
  }

  @Override
  public boolean hasChanged() {
    return this.hasChanged;
  }

  @Override
  public void save() {
    this.logger.info("Saving project " + this.getName() + " to" + this.getFileLocation());
    Document projectDoc = this.createDocumentForProject();
    try {
      Writer w = new FileWriter(this.fileLocation);
      w.write(projectDoc.toXML());
      w.close();
    } catch (IOException e) {
      this.logger.error("Error Writing to file " + this.getFileLocation() + " : " + e.getLocalizedMessage());
    }

  }

  @Override
  public void addResources(File[] files) {
    for (int i = 0; i < files.length; i++) {
      this.logger.debug("Starting add resource process for " + files[i].getAbsolutePath());
      AddResourceDialog ard = AddResourceDialog.getDefaultAddResourceDialog(files[i].getName());
      ard.setVisible(true);
      this.clobberResources.get(0).getConnection();

    }

    // create a tabbed dialog - one tab for each connection type each tab is
    // supplied the cached connections of that type

  }

}
