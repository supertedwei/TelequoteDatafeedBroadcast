package datafeed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alvin
 */
public class Counter {
    String counterid;
    String symbol;
    String bid; // Datatype: 129
    String ask; // Datatype: 130
    String last; // Datatype: 133
    String change; // Datatype: 140
    String high; // Datatype: 144
    String low; // Datatype: 145
    String prev_close; // Datatype: 153
    String open; // Datatype: 154
    String time; // Datatype: 161
    Boolean hasupdate; 
    
    public Counter() {
      this.hasupdate = false;
      this.bid = ""; 
      this.ask = ""; 
      this.last = ""; 
      this.change = ""; 
      this.high = ""; 
      this.low = ""; 
      this.prev_close = ""; 
      this.open = "";
      this.time = ""; 
    }
    
    //--------------------------------------------------------------------------
    boolean update(int datatype, String value) {
        
        value = cleanValue(value);              
        
        switch(datatype) {
        
            case 129: // bid
                if (!this.bid.equals(value)) {
                    this.bid = value;
                    //setUpdate(true);
                }
                break;

            //--------------------------------------------------------------------------
            case 130: // ask
                if (!this.ask.equals(value)) {
                    this.ask = value;
                    //setUpdate(true);
                } 
                break;

            //--------------------------------------------------------------------------
            case 133: // last
                if (!this.last.equals(value)) {
                    this.last = value;
                    //setUpdate(true); // 'Change' will update the prices
                }          
                break;

            //--------------------------------------------------------------------------
            case 140: // change (calculated)
                //### Do not use feed provided 'change' because the value is often
                //### not the sum of (last - prev_close)
                
                if (!this.change.equals(value)) {
                    this.change = value;
                    setUpdate(true);
                }
                break;

            //--------------------------------------------------------------------------
            case 144: // high
                if (!this.high.equals(value)) {
                    this.high = value;
                    setUpdate(true);
                }
                break;

            //--------------------------------------------------------------------------
            case 145: // low
                if (!this.low.equals(value)) {
                    this.low = value;
                    setUpdate(true);
                }
                break;
                
            //--------------------------------------------------------------------------
            case 153: // prev_close
                if (!this.prev_close.equals(value)) {
                    this.prev_close = value;
                    setUpdate(true);
                }
                break;

            //--------------------------------------------------------------------------
            case 154: // open
                if (!this.open.equals(value)) {
                    this.open = value;
                    setUpdate(true);
                }
                break;

            //--------------------------------------------------------------------------
            case 161: // time
                
                if (!this.time.equals(value)) {
                        this.time = value;
                        setUpdate(true);                   
                }
                break;                                
        }
        
        //this.showValues();
                
        if (datatype != 161 && datatype != 140) {
            
            //### If high/low/open/prev_close fields are empty, don't send update
            if (this.high.isEmpty() || this.low.isEmpty() || this.open.isEmpty() || prev_close.isEmpty() || time.isEmpty()) {
                return false;
            }
            else {
                return this.hasupdate;               
            }
        }
        else
        {
            return false;
        }
    }
    
    //--------------------------------------------------------------------------
    //### Strips errornous characters from value
    private String cleanValue(String value) {
        Pattern pattern = Pattern.compile("[^0-9\\.:\\-]"); // Only -/./:/[0-9] are accepted
        Matcher matcher = pattern.matcher(value);
        return matcher.replaceAll("");     
    }
    
    //--------------------------------------------------------------------------
    void setUpdate(boolean status) {
        this.hasupdate = status;
    }
    
    //--------------------------------------------------------------------------
    void showValues() {
        String temp = this.symbol+"|B"+this.bid+"|"+this.last+"|A"+this.ask+"|"+this.change
                    + "|H"+this.high+"|L"+this.low+"|OP"+this.open+"|P"+this.prev_close
                    + "|"+this.time;
        
        System.out.print("\n"+temp);
    }
}
