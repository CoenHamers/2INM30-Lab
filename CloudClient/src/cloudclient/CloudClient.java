package cloudclient;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.io.FileSaver;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class CloudClient {

    final static private String hostname = "localhost";
    final static private int portnumber = 4444;
    final static String print = "July 2015, North-Dakotha";

    static void server() throws IOException {
        ServerSocket ssocket = new ServerSocket(portnumber);
        Socket socket = ssocket.accept();
        
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        DataInputStream dis = new DataInputStream(bis);
        
        String textline = dis.readUTF();
        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();

            files[i] = new File("C:\\Users\\Administrator\\Pictures\\server\\" + fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) {
                bos.write(bis.read());
            }

            bos.close();
        }

        dis.close();
        socket.close();

        for (File f : files) {
            ImagePlus imp = IJ.openImage("C:\\Users\\Administrator\\Pictures\\server\\" + f.getName());
            Font font = new Font("Arial", Font.PLAIN, 18);
            Roi textRoi = new TextRoi(0, 0, textline, font);
            textRoi.setStrokeColor(Color.RED);                         //Set the color of the overlay text to white
            textRoi.setNonScalable(true);                                 //Don’t zoom the overlay text when zooming the image window
            Overlay overlay = new Overlay(textRoi);
            imp.setOverlay(overlay);
            FileSaver fs = new FileSaver(imp);
            fs.saveAsJpeg("C:\\Users\\Administrator\\Pictures\\server\\Edited\\" + f.getName());
        }
    }

    static void client() throws IOException {
        Socket socket = new Socket(hostname, portnumber);

        InputReader ir = new InputReader();
        ir.read();
        ArrayList<File> files = (ArrayList<File>) ir.getFilesinFolder();

        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeUTF(print);
        dos.flush();
        dos.writeInt(files.size());

        for (File file : files) {
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while ((theByte = bis.read()) != -1) {
                bos.write(theByte);
            }

            bis.close();
        }

        dos.close();

        socket.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Thread() {
            public void run() {
                try {
                    server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        client();
    }
}
