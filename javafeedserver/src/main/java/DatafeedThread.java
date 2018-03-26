/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 *
 * @author Alvin Koh
 */
public class DatafeedThread implements Runnable {

    Logger log = LoggerFactory.getLogger(DatafeedThread.class);

    private Socket client;
    private CounterList counterlist;
    private PushServer pushServer;
    
    DatafeedThread (Socket client, CounterList counterlist, PushServer pushServer) {
        this.client = client;
        this.counterlist = counterlist;
        this.pushServer = pushServer;
    }

    public void run() {
        
        BufferedReader in = null;
        String line;
        
        System.out.println("\n[SERVER] Client connected from " + client.getInetAddress().getHostAddress());
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line = in.readLine()) != null)
            {
                log.debug("Counter received : " + line);
                this.counterlist.parse(line, pushServer);
            }
            in.close();
        } catch (IOException ex) {
            System.err.println("\n[SERVER] Unable to read data from " + client.getInetAddress().getHostAddress());
        }
        finally {
            System.err.println("\n[SERVER] Feed client from " + client.getInetAddress().getHostAddress() + " disconnected.");            
        }
    }
    
}
