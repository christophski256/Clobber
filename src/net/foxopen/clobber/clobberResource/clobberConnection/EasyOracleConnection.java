package net.foxopen.clobber.clobberResource.clobberConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;


public class EasyOracleConnection 
{
  
  private Connection conn;
  private Logger l;
  private OracleStatement stmt;
  private OracleResultSet ors;
  
  public EasyOracleConnection(String connectionString, String userName, String password, Logger l) throws SQLException
  {
    conn = DriverManager.getConnection(connectionString, userName, password);    
    this.l = l;
    stmt = null;
    ors = null;
  }
  
  public void executeQuery(String SQL) throws SQLException
  {
    if(stmt != null) stmt.close(); 
    stmt = (OracleStatement) conn.createStatement(); 
    stmt.executeQuery(SQL);
    ors = (OracleResultSet) stmt.getResultSet();
  }
  
  public void execute(String SQL) throws SQLException
  {
    if(stmt != null) stmt.close();
    stmt = (OracleStatement) conn.createStatement();
    stmt.execute(SQL);
  }
  
  public boolean next() throws SQLException
  {
    return ors == null ? false : ors.next();
  }
  
  public OracleResultSet getResult()
  {
    return ors;
  }
  
  public void dispose()
  {
    try
    {
      ors.close();
      stmt.close();
      conn.close();
    }
    catch(Exception e)
    {
      l.warn(e.getStackTrace());
    }
  }
  
}
