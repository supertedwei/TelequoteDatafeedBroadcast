import java.io.IOException;
import java.sql.*;

public class DatafeedApplication {
           
    public static void main(String[] args) {
        /***********************************************************************
        * DATABASE CONNECTION
        ***********************************************************************/        
        String customhost = args.length > 0 ? args[0] : "localhost";
        Database db = new Database(customhost);
        Connection connection;
        
        /***********************************************************************
        * TIMEZONE (UTC)
        ***********************************************************************/
        try {
            connection = db.getConnection();
            Statement statement;
            if (connection != null)
            {
                statement = connection.createStatement();

                System.out.print("[INFO] Setting Database timezone to UTC/GMT: ");
                String query = "SET time_zone = '+00:00';";
                statement.addBatch(query);
                query = "SET autocommit = 0;";
                statement.addBatch(query);
                statement.executeBatch();
                statement.clearBatch();
                
                query = "SELECT NOW() AS currenttime;";
                ResultSet rs = statement.executeQuery(query);
                if (rs.first()) {
                    System.out.println(rs.getString("currenttime"));
                }
                statement.close();
            } else {
                System.err.println("[ERROR] Please check if database connection is valid. Exiting...");                
                System.exit(1);
            }
                       
        } catch (SQLException ex) {
            System.err.println("[ERROR] Unable to set Time Zone on Database!");
        }
        
        /***********************************************************************
        * COUNTERLIST
        ***********************************************************************/
        CounterList counterlist = new CounterList();
        counterlist.setDatabase(db);
        counterlist.fetchCounters();                
        
        /***********************************************************************
        * START FEED SERVER
        ***********************************************************************/
        if (!counterlist.counters.isEmpty()) {
         
           DatafeedServer feedserver = new DatafeedServer(4200);
           PushServer pushServer = new PushServer(3300);
           pushServer.start();

              if (feedserver.listen()) {                 
                  while (true) {
                    DatafeedThread datafeedthread;                    
                    try {
                        datafeedthread = new DatafeedThread(feedserver.getServer().accept(), counterlist, pushServer);
                        new Thread(datafeedthread).start();
                    } catch (IOException e) {
                        System.err.println(e);
                        System.exit(1);
                    }                      
                  }
              }                                  
        }     
    }    
}