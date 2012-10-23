package net.foxopen.clobber;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.swing.JFrame;

import net.foxopen.clobber.ui.ClobberFrame;
import net.foxopen.clobber.ui.ClobberFrameWindowListener;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Aled Lewis
 * 
 */
public class Clobber {

  public static final String DEFAULT_LOGGER_CONFIG = "log4j.rootLogger=DEBUG, ConsoleAppender\n" + "log4j.appender.ConsoleAppender=org.apache.log4j.ConsoleAppender\n"
                                                       + "log4j.appender.ConsoleAppender.layout=org.apache.log4j.PatternLayout\n"
                                                       + "log4j.appender.ConsoleAppender.layout.ConversionPattern=%p %C %M %m%n\n";
  // TODO make this work better
  public static JFrame       clobberFrame;

  /**
   * It's a main method
   * 
   * @param args
   *          The arguments
   */

  public static void main(String[] args) {

    String loggerPath = System.getProperty("user.home") + "/Clobber/.loggerConfig";
    InputStream loggerConfig = null;
    try {
      loggerConfig = loadLoggerConfig(loggerPath);
    } catch (IOException e) {
      System.err.println("Failed to instantiate logging");
      e.printStackTrace();
      System.exit(-1);
    }
    PropertyConfigurator.configure(loggerConfig);
    Logger l = Logger.getLogger(Clobber.class);

    String configPath = System.getProperty("user.home") + "/Clobber/.ClobberConfig.xml";
    boolean wroteNewConfig = false;

    File configFile = new File(configPath);
    if (!configFile.exists()) {
      writeNewConfig(configPath);
      wroteNewConfig = true;
    }

    if (wroteNewConfig) {
      l.info("Wrote new config file to " + configPath);
    }
    l.debug("Config Path: " + configPath);
    l.info("Starting Clobber");

    ClobberModel cm = new ClobberModel(configPath);
    l.info("Clobber model initialised, creating gui");

    clobberFrame = new ClobberFrame(cm);

    clobberFrame.addWindowListener(new ClobberFrameWindowListener(cm));
    cm.start();
    l.info("Started listening");
    clobberFrame.setVisible(true);

  }

  private static InputStream loadLoggerConfig(String loggerPath) throws IOException {
    InputStream loggerConfig = null;
    File f = new File(loggerPath);
    if (f.exists()) {
      loggerConfig = new FileInputStream(f);
    } else {
      Writer w = new FileWriter(f);
      w.write(DEFAULT_LOGGER_CONFIG);
      w.close();
      loggerConfig = new ByteArrayInputStream(DEFAULT_LOGGER_CONFIG.getBytes());
    }

    return loggerConfig;

  }

  private static void writeNewConfig(String configPath) {
    Element root = new Element("CLOBBER_CONFIG");
    root.appendChild(new Element("OPEN_PROJECT_LIST"));

    Document d = new Document(root);
    File f = new File(configPath);
    try {
      Writer w = new FileWriter(f);
      w.write(d.toXML());
      w.close();

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

  }

}
