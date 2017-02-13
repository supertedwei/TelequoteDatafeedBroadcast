package datafeed;

import java.io.*; // Required for IO Exception
import java.net.*; // Required for Server/Client Networking

/**
 *
 * @author Alvin
 */
public class DatafeedServer {
    int port = 4200; // Default listening port to 4200
    ServerSocket server;
    Socket client;
    BufferedReader in;
    Boolean retry = false;    
        
    public DatafeedServer(int port) {
        if (port > 0) {
            this.port = port;
            System.out.println("\n[SERVER] Datafeed Server instance started");
            System.out.println("==================================================");
        }
    }
    
    /***************************************************************************
    * SERVER METHODS
    ***************************************************************************/    
    public boolean listen() {
        
        if (!this.retry) {
            System.out.print("[SERVER] Listening on port " + this.port + ": ");         
        } else {
            System.out.print("\n[SERVER] Restarting to listen on port " + this.port + ": ");
        }
        try {        
            server = new ServerSocket(this.port);
            System.out.println("[Success]");
            return true;            
        } catch (IOException e) {
            System.err.println("[Failure] - Port in use!");
            System.exit(1);
        }                 
        return false;
    }
    
    //--------------------------------------------------------------------------
    public ServerSocket getServer() {
        return this.server;
    }
        
    //--------------------------------------------------------------------------
    public boolean close() {
        if (server.isBound()) {
            try {
                server.close();
                return true;
            } catch (IOException e) {
                System.err.println(e);                
                return false;
            }            
        }
        return true;
    }
    
    //--------------------------------------------------------------------------
    protected void setRetry(Boolean status) {
      this.retry = status;
    }
}
