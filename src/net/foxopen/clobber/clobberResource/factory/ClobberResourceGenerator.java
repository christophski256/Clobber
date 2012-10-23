package net.foxopen.clobber.clobberResource.factory;

import java.io.File;
import java.util.List;

import net.foxopen.clobber.clobberResource.ClobberResource;
import nu.xom.Element;
import nu.xom.Node;

/**
 * Class to instantiate a List of clobber resources based on supplied a supplied
 * DOM
 * 
 * @author aled
 * 
 */
public abstract class ClobberResourceGenerator {

  /**
   * Creates a list of clobber resources from a dom
   * 
   * @param connectionTypeElement
   *          A document containing the different connections for the
   *          clobberProject.
   * @return a list of Clobber resources
   */
  public static List<ClobberResource> createClobberResources(Element connectionTypeElement) {
    ClobberResourceGenerator crf;
    String connectionType = connectionTypeElement.getLocalName();
    if (AutoOracleResourceFactory.typeIdentifier.equals(connectionType)) {
      crf = new AutoOracleResourceFactory();
    } else {
      throw new RuntimeException();
    }
    return crf.createResourcesFromDOM(connectionTypeElement);
  }
  
  public static ClobberResource createClobberResource(File resourceFile){
    
    
    return null;
    
    
    
  }

  /**
   * Method to produce Clobber Resources with a specified connection type
   * 
   * @param connectionTypeDom
   *          A DOM which represents a list of resources with the same
   *          connection type
   * @return A list of ClobberResources
   */
  protected abstract List<ClobberResource> createResourcesFromDOM(Node connectionTypeDom);

}
