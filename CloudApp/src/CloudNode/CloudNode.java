/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudNode;

import WantCloud.Job;
import WantCloud.JobListener;
import WantCloud.Log;
import WantCloud.User;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Coen
 */
public class CloudNode {

    private static boolean stopThread;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Log.WriteInfo("Starting CloudNode");
        ImageJobProcessor processor = new ImageJobProcessor();
        JobListener joblistener = new JobListener(60606);
        
        joblistener.SubscribeOnJobRequests(processor);
        joblistener.StartListening();
        
        stopThread = false;
        Thread monitorThread = new Thread(new Runnable(){
            @Override
            public void run() {
                Log.WriteDebug("Invoke Sleep on MonitorThread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CloudNode.class.getName()).log(Level.SEVERE, null, ex);
                }
                while(!stopThread)
                {
                    try
                    {
                        double cpu = -1;
                        double memoryTotal = -1;
                        double memoryCurrent = -1;
                        double memorySwapTotal = -1;
                        double memorySwapCurrent = -1;

                        String cmdTop = "top -n 2 -b -d 0.2";
                        try
                        {
                            // start up the command in child process
                            String cmd = cmdTop;
                            Process child = Runtime.getRuntime().exec(cmd);

                            // hook up child process output to parent
                            InputStream lsOut = child.getInputStream();
                            InputStreamReader r = new InputStreamReader(lsOut);
                            BufferedReader in = new BufferedReader(r);

                            // read the child process' output
                            String line;
                            int emptyLines = 0;
                            while(emptyLines<3)
                            {
                                line = in.readLine();
                                if (line.length()<1) emptyLines++;
                            }
                            in.readLine();
                            in.readLine();
                            line = in.readLine();
                            System.out.println("Parsing line "+ line);
                            String delims = "%";
                            String[] parts = line.split(delims);
                            System.out.println("Parsing fragment " + parts[0]);
                            delims =" ";

                            parts = parts[0].split(delims);
                            cpu = Double.parseDouble(parts[parts.length-1]);

                            line = in.readLine();
                            System.out.println("Parsing line "+ line);
                            String memdelims = "k ";
                            String[] memparts = line.split(memdelims);
                            System.out.println("Parsing fragment " + memparts[0]);
                            memdelims =" ";

                            String[] memparts2 = memparts[0].split(memdelims);
                            memoryTotal = Double.parseDouble(memparts2[memparts2.length-1]);

                            System.out.println("Parsing fragment " + memparts[1]);
                            memdelims =" ";

                            memparts = memparts[1].split(memdelims);
                            memoryCurrent = Double.parseDouble(memparts[memparts.length-1]);

                            line = in.readLine();
                            System.out.println("Parsing line "+ line);
                            String mem2delims = "k ";
                            String[] mem2parts = line.split(mem2delims);
                            System.out.println("Parsing fragment " + mem2parts[0]);
                            mem2delims =" ";

                            String[] mem2parts2 = mem2parts[0].split(mem2delims);
                            memorySwapTotal = Double.parseDouble(mem2parts2[mem2parts2.length-1]);

                            System.out.println("Parsing fragment " + mem2parts[1]);
                            mem2delims =" ";

                            mem2parts = mem2parts[1].split(mem2delims);
                            memorySwapCurrent = Double.parseDouble(mem2parts[mem2parts.length-1]);
                        }
                        catch (Exception e)
                        { // exception thrown
                            System.out.println("Command failed!");
                        }

                        System.out.println("vmid: balony");
                        System.out.println("CPU Load: " + cpu);
                        System.out.println("Memory Total: " + memoryTotal);
                        System.out.println("Memory Curr: " + memoryCurrent);
                        System.out.println("Memory Total: " + memorySwapTotal);
                        System.out.println("Memory Curr: " + memorySwapCurrent);


                        Socket socket = new Socket("10.141.3.171", 60607);
                        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                        DataOutputStream dos = new DataOutputStream(bos);            
                        dos.writeUTF("balony");
                        dos.flush();           
                        dos.writeUTF(String.valueOf(cpu));
                        dos.flush();           
                        dos.writeUTF(String.valueOf(memoryTotal));
                        dos.flush();           
                        dos.writeUTF(String.valueOf(memoryCurrent));
                        dos.flush();           
                        dos.writeUTF(String.valueOf(memorySwapTotal));
                        dos.flush();           
                        dos.writeUTF(String.valueOf(memorySwapCurrent));
                        dos.flush(); 

                        dos.close();
                        socket.close();
                    }
                    catch (IOException ex)
                    {             Logger.getLogger(CloudNode.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        monitorThread.start();
        
        try {
            System.in.read();
            
            stopThread = true;
            //File file = new File("F:\\Documents\\Github\\2INM30-Lab\\Images\\images");
            //String caption = "Caption";
            
            //ImageEditor ie = new ImageEditor(file);
            //ie.addCaption(caption, 0, 0);
        } catch (IOException ex) {
            Logger.getLogger(CloudNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
