import java.sql.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author Alvin Koh
 */
public class CounterList {
    
    private Database db;
    private Boolean debug = false;
    private Integer iteration = 0; // Flag for pruning
    private Integer limit = 500; // Prune every XXX iteration
    private Integer pruneduration = 30; // minutes
    
    private HashMap<String, String> datatypes;
    HashMap<String, Counter> counters = new HashMap<String, Counter>();
            
    public CounterList() {
        
        this.datatypes = new HashMap<String, String>();
        this.datatypes.put("129", "bid"); // Datatype: 129
        this.datatypes.put("130", "ask"); // Datatype: 130
        this.datatypes.put("133", "last"); // Datatype: 133
        this.datatypes.put("140", "change"); // Datatype: 140
        this.datatypes.put("144", "high"); // Datatype: 144
        this.datatypes.put("145", "low"); // Datatype: 145
        this.datatypes.put("153", "prev_close"); // Datatype: 153
        this.datatypes.put("154", "open"); // Datatype: 154
        this.datatypes.put("161", "time"); // Datatype: 161       
    }    
    
    //--------------------------------------------------------------------------
    void setDatabase(Database db) {
        this.db = db;
    }    
    
    //--------------------------------------------------------------------------
    protected Counter parse(String line) {
      Pattern p = Pattern.compile("^(\\S{1}) (\\S*)\\s*(\\d{3}) (\\S*)$");
      Matcher m = p.matcher(line);
      if (m.matches()) {
            String counterid = m.group(2); // Counterid
            int datatype = Integer.parseInt(m.group(3)); // Data Type
            String value = m.group(4); // Value
            
            //String outputline = counterid + " (" + datatype + ") - " + value;
            //System.out.print(outputline + " ");
            if (datatype != 161) {
                System.out.print(".");         
            }
            
            if (counters.containsKey(counterid)) {
                Counter counter = counters.get(counterid);
                 if(counter.update(datatype, value)) {
                     updatePrice(counterid);
                     return counter;
                 }
            }
      } else if (line.length() > 0) {
        System.out.println("\n[NOTICE]: Invalid input - " + line);
      }
      return null;
    }
    
    //--------------------------------------------------------------------------
    protected void updatePrice(String counterid) {
        Counter counter = counters.get(counterid);
        String query = new String();
        
        try {
            Statement update = this.db.getConnection().createStatement();
          
            query = "REPLACE INTO quote"
                    + " SET"
                    + " symbol = '"+counter.symbol+"',"
                    + " bid = '"+counter.bid+"',"
                    + " ask = '"+counter.ask+"',"
                    + " last = '"+counter.last+"',"
                    + " `change` = '"+counter.change+"',"
                    + " high = '"+counter.high+"',"
                    + " low = '"+counter.low+"',"
                    + " open = '"+counter.open+"',"
                    + " prev_close = '"+counter.prev_close+"',"
                    + " time = NOW()";
                       
            update.addBatch(query);
                    
            query = "INSERT INTO quotelog"
                    + " SET"
                    + " symbol = '"+counter.symbol+"',"
                    + " bid = '"+counter.bid+"',"
                    + " ask = '"+counter.ask+"',"
                    + " last = '"+counter.last+"',"
                    + " `change` = '"+counter.change+"',"
                    + " high = '"+counter.high+"',"
                    + " low = '"+counter.low+"',"
                    + " open = '"+counter.open+"',"
                    + " prev_close = '"+counter.prev_close+"',"
                    + " time = NOW(),"
                    + " timestamp = UNIX_TIMESTAMP()";
                        
            update.addBatch(query);                       
            
            update.addBatch("COMMIT;"); // Optimisation for INNODB

            update.executeBatch();   
            update.clearBatch();
            update.close();
            
            //### CLEANUP
            this.iteration++;
            if (this.iteration % this.limit==0) {
              this.pruneQuoteLog(this.pruneduration);
            }            
            
            //### RESET/UPDATE values
            counter.setUpdate(false); // Reset Flag           
            counter.showValues();           
            
        } catch (SQLException ex) {
            //Logger.getLogger(CounterList.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    //--------------------------------------------------------------------------
    protected void fetchCounters() {

        try {
            Statement statement = this.db.getConnection().createStatement();            

            String query = "SELECT"
                    + " IF (counter.symbol2 != '', counter.symbol2, counter.symbol) AS counterid,"
                    + " counter.symbol,"
                    + " quote.bid, quote.ask, quote.last, quote.change,"
                    + " quote.high, quote.low, quote.prev_close, quote.open,"
                    + " DATE_FORMAT(quote.time,'%H:%i:%s') AS time"
                    + " FROM"
                    + " counter LEFT JOIN quote USING (symbol)"
                    + " WHERE"
                    + " active = 1"
                    + " ORDER BY symbol ASC";
            
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
              Counter temp = new Counter();
              temp.counterid = rs.getString("counterid");
              temp.symbol = rs.getString("symbol");
              temp.bid = rs.getString("bid") != null ? rs.getString("bid") : "";
              temp.ask = rs.getString("ask") != null ? rs.getString("bid") : "";
              temp.last = rs.getString("last") != null ? rs.getString("bid") : "";
              temp.change = rs.getString("change") != null ? rs.getString("bid") : "";
              temp.high = rs.getString("high") != null ? rs.getString("bid") : "";
              temp.low = rs.getString("low") != null ? rs.getString("bid") : "";
              temp.prev_close = rs.getString("prev_close") != null ? rs.getString("bid") : "";
              temp.open = rs.getString("open") != null ? rs.getString("bid") : "";
              temp.time = rs.getString("time") != null ? rs.getString("time") : "";
              this.counters.put(temp.counterid, temp);
              temp.showValues();
              temp = null;
            }
            rs.close();
            statement.close();
            System.out.println();
            
        } catch (Exception ex) {
            System.out.println("[ERROR] Failed to connect to Database!");
            System.out.println(ex);
        }
    }

    //--------------------------------------------------------------------------    
    void pruneQuoteLog(Integer minutes) {
        minutes = minutes != null ? minutes : 30;
        this.iteration = 0; // Reset
        try {
            String query = "DELETE FROM quotelog WHERE timestamp < (UNIX_TIMESTAMP(NOW()-INTERVAL ? MINUTE))";
            PreparedStatement statement = this.db.getConnection().prepareStatement(query);
            statement.setInt(1, minutes);
            System.out.print("\n[INFO] Starting prune....");
            long starttime = System.currentTimeMillis();             
            statement.execute();
            long totaltime = (System.currentTimeMillis()-starttime);
            System.out.println("\n[INFO] Total rows pruned from quotelog ("+ totaltime +" ms): " + statement.getUpdateCount());

            statement.close();
            
        } catch (SQLException ex) {
            System.err.println("\n[ERROR] QuoteLog pruning failed " + ex);
        }        
    }
    
}
