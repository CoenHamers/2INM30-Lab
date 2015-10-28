/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pieter
 * Listens to requests for incoming jobs
 */
public class JobListener {
    
    ServerSocket ssocket;
    int port;
    Thread listeningThread;
    volatile boolean stopThread;
    
    public JobListener(int portToUse)
    {
        port = portToUse;
        stopThread = false;
    }
    
    public void StartListening()
    {
        try {
            ssocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
            Log.WriteError(ex.getMessage());
            Log.WriteError("Will not start listening to incoming requests.");
        }
        
        listeningThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!stopThread)
                {
                    Log.WriteDebug("Started listening to incoming request.");
                    try {
                        Socket socket = ssocket.accept();
                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                        DataInputStream dis = new DataInputStream(bis);
                        String textline = dis.readUTF();
                        Log.WriteDebug("Received line: " + textline);
                        dis.close();
                        bis.close();
                        socket.close();                        
                    } catch (IOException ex) {
                        if(!stopThread)
                            Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
                        else
                            Log.WriteDebug("Server Socket exception was expected.");
                    }
                    Log.WriteDebug("Received incoming request.");
                }
            }
        });
          
        listeningThread.start();
    }
    
    public void StopListening()
    {
        stopThread = true;
        try {
            ssocket.close();
        } catch (IOException ex) {
            Logger.getLogger(JobListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
