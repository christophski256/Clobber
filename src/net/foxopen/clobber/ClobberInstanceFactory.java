package net.foxopen.clobber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.foxopen.clobber.exception.ClobberInstanceInitialisationException;
import net.foxopen.clobber.exception.ClobberProjectInitialisationException;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;

/**
 * Class for the creation of a clobber instance
 * 
 * @author fiviumuser
 * 
 */
public class ClobberInstanceFactory {

  /**
   * Creates a new clobber instance and uses the configuration path specified
   * 
   * @param configFilePath
   *          The String representation of the config file
   * @return The new Clobber instance
   * @throws ClobberInstanceInitialisationException
   *           When the initialisation fails in a caught way.
   */
  public ClobberInstance createClobberInstance(String configFilePath) throws ClobberInstanceInitialisationException {
    Builder b = new Builder();
    Document configDocument;
    try {
      configDocument = b.build(configFilePath);
    } catch (ParsingException | IOException e) {
      throw new ClobberInstanceInitialisationException(e);
    }
    List<ClobberProject> clobberProjects = new ArrayList<ClobberProject>();
    Nodes openProjects = configDocument.query("/CLOBBER_CONFIG/OPEN_PROJECT_LIST/OPEN_PROJECT");
    for (int i = 0; i < openProjects.size(); i++) {
      try {
        clobberProjects.add(ClobberProjectImpl.createClobberProjectFromFile(openProjects.get(i).query("PROJECT_FILE_LOCATION").get(0).getValue()));
      } catch (ClobberProjectInitialisationException e) {
        e.printStackTrace();
      }
    }

    return new ClobberInstance(clobberProjects);

  }

}
