package net.foxopen.clobber.clobberResource.factory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.foxopen.clobber.clobberResource.ClobberResource;
import net.foxopen.clobber.clobberResource.clobberConnection.AutoOracleConnection;
import net.foxopen.clobber.clobberResource.clobberConnection.ClobberConnection;
import net.foxopen.clobber.clobberResource.fileSystemLocation.FileSystemLocation;
import net.foxopen.clobber.clobberResource.fileSystemLocation.FileSystemLocations;
import nu.xom.Node;
import nu.xom.Nodes;

/**
 * Factory for creating a clobber resource with an Auto Oracle Connection
 * 
 * @author aled
 * 
 */
public class AutoOracleResourceFactory extends ClobberResourceGenerator {

  /**
   * Element name which represents an auto oracle connection
   */
  public static final String typeIdentifier = "AUTO_ORACLE_CLOBBERS";

  @Override
  protected List<ClobberResource> createResourcesFromDOM(Node connectionTypeDom) {

    List<ClobberResource> clobs = new ArrayList<ClobberResource>();

    Nodes resources = connectionTypeDom.query("RESOURCE_LIST/RESOURCE");

    for (int i = 0; i < resources.size(); i++) {
      Node resource = resources.get(i);
      String xpath = "CONFIGURATION_LIST/CONFIGURATION[ID='" + resource.query("CONFIGURATION_ID").get(0).getValue() + "']";

      Node resourceConfig = connectionTypeDom.query(xpath).get(0);
      ClobberConnection cc = new AutoOracleConnection(resource.query("TABLE_TYPE_INSTANCE_MNEM").get(0).getValue(), resourceConfig
          .query("CONFIG_DATABASE_CONNECTION_STRING").get(0).getValue(), resourceConfig.query("CONFIG_USERNAME").get(0).getValue(), resourceConfig
          .query("CONFIG_PASSWORD").get(0).getValue(), resourceConfig.query("CONFIG_TABLE").get(0).getValue());
      FileSystemLocation fileLocation = FileSystemLocations.createFileSystemLocation(resource.query("FILE_SYSTEM_LOCATION").get(0));
      String lastClobbedString = resource.query("LAST_CLOBBED").get(0).getValue();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      Date lastClobbed = null;
      try {
        lastClobbed = df.parse(lastClobbedString);
      } catch (ParseException e) {
        lastClobbed = new Date(0);
      }
      clobs.add(new ClobberResource(fileLocation, cc, "true".equals(resource.query("IS_ACTIVE").get(0).getValue()), lastClobbed));
    }
    resources = null;

    return clobs;
  }

}
