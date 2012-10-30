package net.foxopen.clobber.clobberResource.clobberConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import net.foxopen.clobber.clobberResource.ClobberResource;
import net.foxopen.clobber.clobberResource.ResourceSerialiser;
import net.foxopen.clobber.exception.ClobbingFailedException;

import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathException;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.apache.log4j.Logger;

/**
 * Represents a connection to an Oracle database loaded from config stored on an
 * Oracle database.
 * 
 * @author aled
 * 
 */
public class AutoOracleConnection extends ClobberConnection {

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((configConnectionString == null) ? 0 : configConnectionString.hashCode());
    result = prime * result + ((configTable == null) ? 0 : configTable.hashCode());
    result = prime * result + ((configUserName == null) ? 0 : configUserName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AutoOracleConnection other = (AutoOracleConnection) obj;
    if (configConnectionString == null) {
      if (other.configConnectionString != null)
        return false;
    } else if (!configConnectionString.equals(other.configConnectionString))
      return false;
    if (configTable == null) {
      if (other.configTable != null)
        return false;
    } else if (!configTable.equals(other.configTable))
      return false;
    if (configUserName == null) {
      if (other.configUserName != null)
        return false;
    } else if (!configUserName.equals(other.configUserName))
      return false;
    return true;
  }

  private String                     tableTypeInstance;
  private String                     configConnectionString;
  private String                     configUserName;
  private String                     configPassword;
  private String                     configTable;
  private FileNameConversionStrategy primaryKeyStrategy;

  /**
   * 
   * @param tableTypeInstance
   *          The table type instance from the config xml
   * @param configConnectionString
   *          The connection string used to connect to the Oracle database
   * @param configUserName
   *          The username to use for the above connection
   * @param configPassword
   *          The password to use for the above connection
   * @param configTable
   *          The table in the database to use.
   */
  public AutoOracleConnection(String tableTypeInstance, String configConnectionString, String configUserName, String configPassword, String configTable) {
    this.tableTypeInstance = tableTypeInstance;
    this.configConnectionString = configConnectionString;
    this.configUserName = configUserName;
    this.configPassword = configPassword;
    this.configTable = configTable;
    this.primaryKeyStrategy = new RemoveExtensionFileNameConversionStrategy();
  }

  @Override
  public void doAction(InputStream newValueStream, String fileName) throws ClobbingFailedException {

    // TODO may be worth abstracting out the oracle connection stuff
    Logger l = Logger.getLogger(AutoOracleConnection.class);
    l.debug("Using an autotomatic Oracle connection to clob " + fileName);
    EasyOracleConnection econn = null;
    EasyOracleConnection eresourceConnection = null;
    Connection conn = null;
    Connection resourceConnection = null;
    String resourceTableName;
    String resourceConnectionString;
    String resourceUser;
    String resourcePassword;
    String plSQL;
    String lobType;
    Node resourceDatabaseElement;
    Builder b = new Builder();
    // create oracle connection
    String driverName = "oracle.jdbc.driver.OracleDriver";
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      throw new ClobbingFailedException(e);
    }
    try {
      
      //econn = new EasyOracleConnection(this.configConnectionString, this.configUserName, this.configPassword, l); 
      // load config from xmltype column on database
      conn = DriverManager.getConnection(this.configConnectionString, this.configUserName, this.configPassword);

      OracleStatement stmt = (OracleStatement) conn.createStatement();
      
      String statementString = "SELECT (xml_data).getClobVal() " + "FROM " + this.configUserName + "." + this.configTable + " " + "WHERE name = 'CLOBBER_RESOURCE_MASTER'";

      l.debug("Executing " + statementString);
      stmt.execute(statementString);
      OracleResultSet ors = (OracleResultSet) stmt.getResultSet();
      ors.next();
      
      //econn.execute(statementString);
      //econn.next();
      

      InputStream clobberResourceMasterStream = ors.getCLOB(1).asciiStreamValue();
      //InputStream clobberResourceMasterStream = econn.getResult().getCLOB(1).asciiStreamValue();

      Node clobberResourceMasterDOM = b.build(clobberResourceMasterStream);

      // get necessary data from xml
      l.debug("Getting plsql for update");

      Node tableTypeInstanceNode;

      String xpath;

      xpath = "/CLOBBER_RESOURCE_MASTER/TABLE_TYPE_INSTANCE_LIST/TABLE_TYPE_INSTANCE[MNEM = '" + this.tableTypeInstance + "']";
      tableTypeInstanceNode = clobberResourceMasterDOM.query(xpath).get(0);
      xpath = "/*/TABLE_TYPE_LIST/TABLE_TYPE[MNEM = '" + tableTypeInstanceNode.query("TABLE_TYPE_MNEM").get(0).getValue() + "']/PLSQL_TO_UPDATE_LOB";
      plSQL = clobberResourceMasterDOM.query(xpath).get(0).getValue();
      xpath = "/*/TABLE_TYPE_LIST/TABLE_TYPE[MNEM = '" + tableTypeInstanceNode.query("TABLE_TYPE_MNEM").get(0).getValue() + "']/LOB_TYPE";
      lobType = clobberResourceMasterDOM.query(xpath).get(0).getValue();
      xpath = "/*/DATABASE_LIST/DATABASE[MNEM = '" + tableTypeInstanceNode.query("DATABASE_MNEM").get(0).getValue() + "']";
      resourceDatabaseElement = clobberResourceMasterDOM.query(xpath).get(0);
      resourceTableName = tableTypeInstanceNode.query("TABLE_NAME").get(0).getValue();
      Nodes resourceConnectionNodes = resourceDatabaseElement.query("CONNECTION_STRING");
      if (resourceConnectionNodes.size() == 0) {
        resourceConnectionString = this.configConnectionString;
      } else {
        resourceConnectionString = resourceConnectionNodes.get(0).getValue();
      }
      resourceUser = resourceDatabaseElement.query("USERNAME").get(0).getValue();
      resourcePassword = resourceDatabaseElement.query("PASSWORD").get(0).getValue();

    } catch (SQLException | ParsingException | IOException | XPathException e) {
      e.printStackTrace();
      throw new ClobbingFailedException(e);
    } finally {
      
      //econn.dispose();
      try {
        
        conn.close();
      } catch (SQLException | NullPointerException e) {
        l.warn(e.getStackTrace());
      }
    }

    // replace binds in xml
    plSQL = plSQL.replaceAll(":schema_name", "'" + resourceUser + "'");
    plSQL = plSQL.replaceAll(":table_name", "'" + resourceTableName + "'");
    plSQL = plSQL.replaceAll(":primary_key_value", "'" + this.primaryKeyStrategy.getConvertedFileName(fileName) + "'");

    // connect to database described in xml
    l.debug("Connecting to " + resourceConnectionString + " user: " + resourceUser + " password: " + resourcePassword);
    try {
      
      resourceConnection = DriverManager.getConnection(resourceConnectionString, resourceUser, resourcePassword);
      l.debug("Connected to " + resourceUser);
      l.debug("Statement to execute: \n" + plSQL);
      // set up a temporary LOB
      if ("BLOB".equals(lobType)) {
        BLOB blob = BLOB.createTemporary(resourceConnection, false, BLOB.DURATION_SESSION);
        blob.open(BLOB.MODE_READWRITE);
        OutputStream out = blob.setBinaryStream(0);
        byte[] bytes = new byte[4096];
        int lengthWritten;
        try {
          while ((lengthWritten = newValueStream.read(bytes)) > 0) {
            out.write(bytes, 0, lengthWritten);
            bytes = new byte[4096];
          }
        } catch (IOException e) {
          throw new ClobbingFailedException(e);

        }
        try {
          out.flush();
        } catch (IOException e) {
          throw new ClobbingFailedException(e);
        }
        OraclePreparedStatement ps = (OraclePreparedStatement) resourceConnection.prepareCall(plSQL);
        ps.setBLOB(1, blob);
        ps.executeUpdate();
        resourceConnection.commit();
      } else if ("CLOB".equals(lobType)) {
        CLOB clob = CLOB.createTemporary(resourceConnection, false, CLOB.DURATION_SESSION);
        clob.open(CLOB.MODE_READWRITE);
        OutputStream out = clob.setAsciiStream(0);
        byte[] bytes = new byte[4096];
        int lengthWritten;
        try {
          while ((lengthWritten = newValueStream.read(bytes)) > 0) {
            out.write(bytes, 0, lengthWritten);
            bytes = new byte[4096];
          }
        } catch (IOException e) {
          throw new ClobbingFailedException(e);
        }
        try {
          out.flush();
        } catch (IOException e) {
          throw new ClobbingFailedException(e);
        }
        OraclePreparedStatement ps = (OraclePreparedStatement) resourceConnection.prepareCall(plSQL);
        ps.setCLOB(1, clob);
        ps.executeUpdate();
        resourceConnection.commit();
      }

      l.debug("Clob success");

      // set up the clobbing statement

    } catch (SQLException e) {
      l.warn(e.getMessage());
      throw new ClobbingFailedException(e);
    } finally {
      try {
        resourceConnection.close();
      } catch (SQLException | NullPointerException e) {
        l.warn(e.getMessage());
      }
    }

  }

  @Override
  public InputStream getRemoteResourceInputStream() {
    throw new RuntimeException("Oops not implemented");

  }

  @Override
  public String getConnectionTypeElementName() {
    return "AUTO_ORACLE_CLOBBERS";
  }

  @Override
  public String getConnectionName() {
    return "Auto Oracle: " + this.tableTypeInstance;
  }

  @Override
  public boolean isDifferentToActionTarget(Reader localResource) {

    return false;
  }

  static {
    ClobberConnection.registerSerialiser(AutoOracleConnection.class, new AutoOracleResourceSerialiser());
    ClobberConnection.registerAddResourcePane(AutoOracleConnection.class, new AutoOracleAddResourcePanel());
  }
  


   static class AutoOracleResourceSerialiser implements ResourceSerialiser {
    @Override
    public Element getElementRepresentation(List<ClobberResource> resources) {
      Logger l = Logger.getLogger(this.getClass());
      l.debug("Creating Auto Oracle resources' serialisation");
      Element autoOracleClobbersElement = new Element("AUTO_ORACLE_CLOBBERS");
      l.debug("Creating connection details");
      int numConnections = 0;
      Element configurationList = new Element("CONFIGURATION_LIST");
      Element resourceList = new Element("RESOURCE_LIST");
      Map<AutoOracleConnection, Integer> connectionStringToIdMap = new HashMap<AutoOracleConnection, Integer>();
      for (ClobberResource resource : resources) {
        AutoOracleConnection connection = (AutoOracleConnection) resource.getConnection();
        Integer connectionNumber = connectionStringToIdMap.get(connection);
        if (connectionNumber == null) {
          connectionNumber = ++numConnections;
          l.debug("Creating connection number " + connectionNumber);

          // buid up connection element
          Element configuration = new Element("CONFIGURATION");
          Element id = new Element("ID");
          id.appendChild("" + connectionNumber);
          Element connectString = new Element("CONFIG_DATABASE_CONNECTION_STRING");
          connectString.appendChild(connection.configConnectionString);
          Element user = new Element("CONFIG_USERNAME");
          user.appendChild(connection.configUserName);
          Element password = new Element("CONFIG_PASSWORD");
          password.appendChild(connection.configPassword);
          Element configTable = new Element("CONFIG_TABLE");
          configTable.appendChild(connection.configTable);

          configuration.appendChild(id);
          configuration.appendChild(connectString);
          configuration.appendChild(user);
          configuration.appendChild(password);
          configuration.appendChild(configTable);
          configurationList.appendChild(configuration);

          // register
          connectionStringToIdMap.put(connection, connectionNumber);
        }

        Element resourceElem = new Element("RESOURCE");
        Element configurationID = new Element("CONFIGURATION_ID");
        configurationID.appendChild(connectionNumber.toString());
        Element filesystemElement = resource.getFilesystemElement();
        Element tableTypeInstanceMnem = new Element("TABLE_TYPE_INSTANCE_MNEM");
        tableTypeInstanceMnem.appendChild(connection.tableTypeInstance);
        Element isActive = new Element("IS_ACTIVE");
        if (resource.isActive()) {
          isActive.appendChild("true");

        } else {
          isActive.appendChild("false");
        }

        Element lastClobbed = new Element("LAST_CLOBBED");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        lastClobbed.appendChild(df.format(resource.getLastClobbed()));

        resourceElem.appendChild(configurationID);
        resourceElem.appendChild(filesystemElement);
        resourceElem.appendChild(tableTypeInstanceMnem);
        resourceElem.appendChild(isActive);
        resourceElem.appendChild(lastClobbed);

        resourceList.appendChild(resourceElem);
      }

      // add resource element

      autoOracleClobbersElement.appendChild(configurationList);
      autoOracleClobbersElement.appendChild(resourceList);

      return autoOracleClobbersElement;
    }
  }

  @Override
  public String getConnectionType() {
    return this.getConnectionTypeElementName();
  }

}
