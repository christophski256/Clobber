package net.foxopen.clobber;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JFrame;

import net.foxopen.clobber.exception.ClobberInstanceInitialisationException;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.log4j.Logger;

/**
 * Covers the creation of an application
 * 
 * @author Aled Lewis
 * 
 */

public class ClobberModel {

  private ClobberInstance clobberInstance;
  private String          configFileLocation;
  private Logger          logger;
  private JFrame          clobberFrame;

  public ClobberModel(String configFileLocation) {
    this.configFileLocation = configFileLocation;
    this.initialise(configFileLocation);
    this.logger = Logger.getLogger(this.getClass());
  }

  public ClobberInstance getClobberInstance() {
    return this.clobberInstance;
  }

  private void initialise(String configPath) {
    ClobberInstanceFactory cif = new ClobberInstanceFactory();

    try {
      this.clobberInstance = cif.createClobberInstance(configPath);

    } catch (ClobberInstanceInitialisationException e) {
      throw new RuntimeException("Initialisation Failed");
    }
  }

  /**
   * Starts the clobber model
   */
  public void start() {
    this.clobberInstance.startAllProjectsListening();
  }

  public void shutdown() {
    this.logger.debug("Checking if the application should close");

    boolean shouldClose = this.clobberInstance.doCloseOperations();
    if (shouldClose) {

      this.saveConfig();
      this.logger.info("Exiting");
      System.exit(0);
    }
  }

  private void saveConfig() {
    this.logger.info("Saving clobber config");
    Element root = new Element("CLOBBER_CONFIG");
    Element openProjects = this.clobberInstance.getOpenProjectsElement();
    root.appendChild(openProjects);
    Document configDocument = new Document(root);
    logger.info("Saving instance config to " + this.configFileLocation);
    Writer configWriter;
    try {
      configWriter = new FileWriter(this.configFileLocation);
      configWriter.write(configDocument.toXML());
      configWriter.close();
    } catch (IOException e) {
      this.logger.error("Writing config failed: " + e.getLocalizedMessage());

    }

  }

  public void openProject(File file) {
    this.logger.info("Opening Project: " + file.getName());

  }

  public void setFrame(JFrame clobberWindow) {
    this.clobberFrame = clobberWindow;

  }

  public JFrame getFrame() {
    return this.clobberFrame;
  }
}
