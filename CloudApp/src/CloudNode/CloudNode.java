/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudNode;

import java.io.File;

/**
 *
 * @author Coen
 */
public class CloudNode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File file = new File("Path to file");
        String caption = "";
        
        ImageEditor ie = new ImageEditor(file);
        ie.addCaptionMultiples(caption);
        
    }
    
}
