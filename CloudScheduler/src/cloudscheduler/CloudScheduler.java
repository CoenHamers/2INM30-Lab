/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 * @author Administrator
 */
public class CloudScheduler {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        if(args[0].equals("") || args[0].equals("--help") || args[0].equals("-h"))
        {
            PrintArgumentList();
            return;
        }
        
        Log.WriteInfo("This is CloudScheduler, an application developed for 2IMN30"); 
        Log.WriteInfo("Created by C. Hamers and P. Gijsbers");         
        
        Client oneClient;
        try {
            String url = "http://fs3.das4.tudelft.nl:2633/RPC2";
            String user = args[0]; //cld9999
            String password = args[1]; //XXXXXXXX
            int port = Integer.parseInt(args[2]); //XXXXXXXX
            
            oneClient = new Client(user+":"+password, url);
            Log.WriteInfo("Connected to " + url + " as user " + user);
            OneResponse version = oneClient.get_version();
            Log.WriteOneResponse(version);
            
            Log.WriteDebug("Creating Scheduler");
            Scheduler scheduler = new Scheduler(oneClient);
            JobListener listener = new JobListener(port);
            listener.SubscribeOnJobRequests(scheduler);
            listener.StartListening();
            
            Log.WriteInfo("Press enter to exit.");
            try {
                System.in.read();
                listener.StopListening();
                scheduler.Close();
            } catch (IOException ex) {
                Logger.getLogger(CloudScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (ClientConfigurationException ex) {
            Logger.getLogger(CloudScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private static void PrintArgumentList()
   {       
        System.out.println("Please start this program with the following arguments:");
        System.out.println("1. Your OpenNebula username (cld9999)");
        System.out.println("2. Your OpenNebula password (xxxxxxx)");
        System.out.println("3. The portnumber you want incoming to listen to for jobrequests.");
        System.out.println("   If the portnumber is not specified, 4444 will be used.");
   }
}
