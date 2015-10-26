/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageEditing;

import ij.*;
import ij.gui.*;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Administrator
 */
public class Image {

    public void Image() {
        ImagePlus imp = IJ.openImage("C:\\Users\\Administrator\\Documents\\GitHub\\2INM30-Lab\\CloudApp\\src\\connection1.png");
        Font font = new Font("Arial", Font.PLAIN, 10);
        Roi textRoi = new TextRoi(0, 0, "Test Text", font);
        textRoi.setStrokeColor(Color.black);                         //Set the color of the overlay text to white
        textRoi.setNonScalable(true);                                 //Don’t zoom the overlay text when zooming the image window
        Overlay overlay = new Overlay(textRoi);
        imp.setOverlay(overlay);
        imp.show();
    }

    public static void main(String args[]) {

        new Image().Image();
    }
}
