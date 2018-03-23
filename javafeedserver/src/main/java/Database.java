import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 *
 * @author Alvin Koh
 */
public class Database {

    Logger log = LoggerFactory.getLogger(Database.class);
    
    String host = "localhost";
    String port = "3306";
    String user = "realtime";
    String password = "realtime";
    String database = "realtime";       
    String connectionURL;
    Connection connection = null;    
    
    Long busytimestamp = 0L;
    
    public Database(String customhost) {
        
        /***********************************************************************
        * Load Database Driver
        ***********************************************************************/
        try {
            // Load JBBC driver "com.mysql.jdbc.Driver".
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.err.println("[ERROR] Database drivers could not be loaded!");
            System.exit(1);
        }           
        this.host = customhost;
        this.connectionURL = "jdbc:mysql://"+this.host+":"+this.port+"/"+this.database+"?connectTimeout=3";               
    }
        
    //----------------------------------------------------------------------    
   
    public Connection getConnection() throws SQLException {     
        
        if (this.connection != null && !this.connection.isClosed()) {       
            return this.connection;
        }       
        else
        {   
            if (!this.isBusy()) {
                try {
                    setBusyTimestamp();
                    log.info("Connecting to "+this.host+":"+this.port);
                    log.info("==================================================");
                    connection = DriverManager.getConnection(this.connectionURL, this.user, this.password);            
                } catch (SQLException ex) {
                    log.error("Database communication error");
                }     
                return this.connection;
                
            }
            return this.connection;
        }
    }
    
    //----------------------------------------------------------------------
    private void setBusyTimestamp() {
        this.busytimestamp =  (System.currentTimeMillis()/1000);
    }
    
    //----------------------------------------------------------------------
    private Boolean isBusy() {
        Long currenttime = (System.currentTimeMillis()/1000);
        if ((currenttime - this.busytimestamp) < 5) {
            return true;
        } else {
            return false;
        }
    }
}
