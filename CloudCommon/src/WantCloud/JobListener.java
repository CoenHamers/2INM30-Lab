/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WantCloud;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;
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
    Vector listeners;
    
    public JobListener(int portToUse)
    {
        port = portToUse;
        stopThread = false;
        listeners = new Vector();
    }
    
    public void SubscribeOnJobRequests(JobProcessor jobEventProcessor)
    {
        listeners.addElement(jobEventProcessor);
    }
        
    public void OnJobRequest(Job jobrequest, User user)
    {
        Enumeration e = listeners.elements();
        while(e.hasMoreElements())
        {
            JobProcessor js = (JobProcessor)e.nextElement();
            js.ProcessJob(jobrequest, user);
        }
    }
    
    public void StartListening()
    {
        try {
            Log.WriteDebug("Start istening on port " + port);
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
                        
                        // TODO: Run this in a separate thread again, so new clients can immediately be responded to
                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                        DataInputStream dis = new DataInputStream(bis);
                        String actionString = dis.readUTF();
                        String caption = dis.readUTF();
                        String id = dis.readUTF();
                        int workloadFactor = dis.readInt();
                        
                        Log.WriteDebug("Connected to " + socket.getInetAddress());
                        Log.WriteDebug("Received line: " + actionString + ", " + caption + " for id " + id + "with workload factor " + workloadFactor);
                        if(!id.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"))
                        {
                            id = UUID.randomUUID().toString();
                        }
                        Log.WriteDebug("Attempting to convert " + id + " into UUID");
                        UUID jobId = UUID.fromString(id);
                        
                        
                        int filesCount = dis.readInt();
                        File[] files = new File[filesCount];

                        for (int i = 0; i < filesCount; i++) {
                            long fileLength = dis.readLong();
                            String fileName = dis.readUTF();

                            Log.WriteDebug("Constructing file object");
                            String baseFolder = "jobs/" + jobId.toString() + "/";
                            if(actionString.equals("result"))
                            {
                                baseFolder += "result/";
                            }
                            files[i] = new File(baseFolder + fileName);
                            
                            Log.WriteDebug("Attempting to create directories");
                            File dirfile = new File(baseFolder);
                            dirfile.mkdirs();
                            
                            Log.WriteDebug("Attempting to write to file");
                            FileOutputStream fos = new FileOutputStream(files[i]);
                            BufferedOutputStream bos = new BufferedOutputStream(fos);

                            for (int j = 0; j < fileLength; j++) {
                                bos.write(bis.read());
                            }

                            bos.close();
                        }

                        User user = new User(socket.getInetAddress().toString(), "");
                        boolean isRequest = "request".equals(actionString);
                        Job job = new Job(jobId, caption, files, isRequest, workloadFactor);
                        OnJobRequest(job, user);
                        
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
