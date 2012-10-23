package net.foxopen.clobber.clobberResource.clobberConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionCache {

  
  private Map<String, List<ClobberConnection>> cacheMap;
  private static ConnectionCache CACHE;
  
  public ConnectionCache(){
    this.cacheMap = new HashMap<String,List<ClobberConnection>>();
  }
  
  public static void register(String connectionType, ClobberConnection clobberConnection) {
    CACHE.registerInternal(connectionType, clobberConnection);
  }
  
  public static List<ClobberConnection> getConnections(String connectionType){
    return CACHE.getConnectionsInternal(connectionType);
    
  }

  private List<ClobberConnection> getConnectionsInternal(String connectionType) {
    return this.cacheMap.get(connectionType);
  }

  private void registerInternal(String connectionType, ClobberConnection clobberConnection) {
     List<ClobberConnection> connections = this.cacheMap.get(connectionType);
     if( connections == null){
       connections = new ArrayList<ClobberConnection>();
     }
     connections.add(clobberConnection);    
  }

  static{
    CACHE = new ConnectionCache();
  }
}
