package CloudNode;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.io.FileSaver;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

/**
 *
 * @author Coen
 */

/**
 * ImageEditor class used to edit the images in a certain way
 * 
 */
public class ImageEditor {

    final private File file;
    private ImagePlus imp;

    /**
     * The constructor takes the file that needs to be edited as input
     * @param f the file to be edited
     */
    public ImageEditor(File f) {
        file = f;
        createImageJFile();

    }

    /**
     * Adds the caption in multiple places on the image and all the intermediate 
     * results are saved
     * @param caption to be added in multiple places
     */
    public void addCaptionMultiples(String caption) {
        for(int i = 0; i < imp.getWidth(); ++i) {
            for(int j = 0; j < imp.getHeight(); ++j) {
                addCaption(caption, i,j);
                saveImage();
            }
        }
    }

    /**
     * Adds a caption to the image at place x,y
     * @param caption to be added
     * @param x x-coordinate of where the caption should be added
     * @param y y-coordinate of where the caption should be added
     */
    public void addCaption(String caption, int x, int y) {
        Font font = new Font("Arial", Font.PLAIN, 18);
        Roi textRoi = new TextRoi(x, y, caption, font);
        textRoi.setStrokeColor(Color.RED);                         //Set the color of the overlay text to white
        textRoi.setNonScalable(true);                                 //Don’t zoom the overlay text when zooming the image window
        Overlay overlay = new Overlay(textRoi);
        imp.setOverlay(overlay);
    }

    /**
     * Saves the image 
     */
    public void saveImage() {
        FileSaver fs = new FileSaver(imp);
        fs.saveAsJpeg(file.getParent() + imp.getTitle());

    }

    /**
     * Creates and ImageJfile from the File
     */
    private void createImageJFile() {
        imp = IJ.openImage(file.getAbsolutePath());

    }
}
