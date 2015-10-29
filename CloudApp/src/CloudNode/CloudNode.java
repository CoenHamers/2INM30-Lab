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
        File file = new File("F:\\Documents\\Github\\2INM30-Lab\\Images\\images");
        String caption = "Caption";
        
        ImageEditor ie = new ImageEditor(file);
        ie.addCaption(caption, 0, 0);
        
    }
    
}
