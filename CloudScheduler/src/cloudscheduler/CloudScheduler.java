/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Administrator
 */
public class CloudScheduler {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        Log.WriteInfo("This is CloudScheduler, an application developed for 2IMN30"); 
        Log.WriteInfo("Created by C. Hamers and P. Gijsbers"); 
        
        Client oneClient;
        try {
            String url = "http://fs3.das4.tudelft.nl:2633/RPC2";
            String user = ""; //cld9999
            String password = ""; //XXXXXXXX
            
            oneClient = new Client(user+":"+password, url); //tut7WO7e
            Log.WriteInfo("Connected to " + url + " as user " + user);
            
            Log.WriteDebug("Creating Scheduler");
            Scheduler scheduler = new Scheduler(oneClient);
            
            Log.WriteDebug("Start Scheduler TestSequence");
            try {
                scheduler.TestSequence();
                
                //System.out.println("Requesting monitor about VM 40889");
                //OneResponse vmInfoResponse = VirtualMachine.monitoring(oneClient, 40889);
                //WriteOneResponse(vmInfoResponse);        
            } catch (InterruptedException ex) {
                Logger.getLogger(CloudScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (ClientConfigurationException ex) {
            Logger.getLogger(CloudScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
