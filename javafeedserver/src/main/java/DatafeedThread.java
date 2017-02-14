/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;

/**
 *
 * @author Alvin Koh
 */
public class DatafeedThread implements Runnable {
    private Socket client;
    private CounterList counterlist;
    
    DatafeedThread (Socket client, CounterList counterlist) {
        this.client = client;
        this.counterlist = counterlist;
    }

    @Override
    public void run() {
        
        BufferedReader in = null;
        String line;
        
        System.out.println("\n[SERVER] Client connected from " + client.getInetAddress().getHostAddress());
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while ((line = in.readLine()) != null)
            {
             this.counterlist.parse(line);
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
