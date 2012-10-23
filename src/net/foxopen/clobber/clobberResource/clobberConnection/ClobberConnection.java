package net.foxopen.clobber.clobberResource.clobberConnection;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.foxopen.clobber.clobberResource.ResourceSerialiser;
import net.foxopen.clobber.exception.ClobbingFailedException;

/**
 * Interface for an action a Clobber Resource should take when a change is
 * detected.
 *
 * @author aled
 *
 */
public abstract class ClobberConnection {


  /**
   * Do the "clob". Streams to the remote resource whatever is in the
   * newValueStream
   * 
   * @param newValueStream
   *          What to stream to the remote resource.
   * @param fileName
   *          The string path of the file that has changed.
   * @throws ClobbingFailedException
   *           If the clobbing failed for some reason.
   */
  public abstract void doAction(InputStream newValueStream, String fileName) throws ClobbingFailedException;

  /**
   * Gets the remote resource's data in an InputStream.
   * 
   * @return The remote resource's data in an InputStream.
   */
  public abstract InputStream getRemoteResourceInputStream();
  
  /**
   * Returns the connection type that this clobber connection is one of as a valid DOM element name
   * @return The element name version of this clobberConnection
   */
  public abstract String getConnectionTypeElementName();


  /**
   * Returns the human readable name of the connection type.
   * @return The human readable name of the connection type.
   */
  public abstract String getConnectionName();

  public abstract String getConnectionType();
  
  public abstract boolean isDifferentToActionTarget(Reader fileReader);
  
  public void registerWithCache(){
    ConnectionCache.register(this.getConnectionType(), this);
  }
  
  
  //
  private static Map<Class<? extends ClobberConnection>, ResourceSerialiser> serialiserMap = new HashMap<Class<? extends ClobberConnection>,ResourceSerialiser>();
  private static Map<Class<? extends ClobberConnection>, AddResourcePanel> addResourcePaneMap = new HashMap<Class<? extends ClobberConnection>,AddResourcePanel>();
  
  
  protected static void registerSerialiser(Class<? extends ClobberConnection> clobberConnectionClass, ResourceSerialiser resourceSerialiser) {
    ClobberConnection.serialiserMap.put(clobberConnectionClass, resourceSerialiser); 
  }
  
  public static ResourceSerialiser getSerialiser(Class<? extends ClobberConnection> connectionClass){
    return ClobberConnection.serialiserMap.get(connectionClass);
  }

  public static void registerAddResourcePane(Class<? extends ClobberConnection> connectionClass, AddResourcePanel addResourcePane) {
    ClobberConnection.addResourcePaneMap.put(connectionClass,addResourcePane );
  }
  
  public static List<AddResourcePanel> getAllAddResourcePanels(){
    List<AddResourcePanel> arpList = new ArrayList<AddResourcePanel>();
    arpList.addAll(ClobberConnection.addResourcePaneMap.values());
    return arpList;
  }
  
}
