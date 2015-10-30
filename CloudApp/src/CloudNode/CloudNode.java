/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudNode;

import WantCloud.JobListener;
import WantCloud.Log;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Coen
 */
public class CloudNode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Log.WriteInfo("Starting CloudNode");
        ImageJobProcessor processor = new ImageJobProcessor();
        JobListener joblistener = new JobListener(60606);
        
        joblistener.SubscribeOnJobRequests(processor);
        joblistener.StartListening();
        
        try {
            System.in.read();
            //File file = new File("F:\\Documents\\Github\\2INM30-Lab\\Images\\images");
            //String caption = "Caption";
            
            //ImageEditor ie = new ImageEditor(file);
            //ie.addCaption(caption, 0, 0);
        } catch (IOException ex) {
            Logger.getLogger(CloudNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
