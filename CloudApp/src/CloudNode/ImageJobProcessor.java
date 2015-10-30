/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CloudNode;

import WantCloud.Job;
import WantCloud.JobProcessor;
import WantCloud.Log;
import WantCloud.User;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Pieter
 */
public class ImageJobProcessor implements JobProcessor{
    
    @Override
    public void ProcessJob(Job job, User user) {
        Log.WriteDebug("Starting to process job");
        File[] filesToProcess = job.GetFiles();
        Log.WriteDebug("Filecount: " + filesToProcess.length);
        
        for (File f : filesToProcess)
        {
            for(int i = 0; i<job.GetWorkloadFactor(); i++)
            {
                Log.WriteDebug("Processing image " + f.getAbsolutePath());
                ImageEditor ie = new ImageEditor(f);
                    ie.addCaption(job.GetJobDescription(), i, i);

                Log.WriteDebug("Saving image");
                ie.saveImage();
            }
        }        
        
        Log.WriteDebug("Processed job");
        ReturnJobResult(job, user);
    }
    
    private void ReturnJobResult(Job job, User user)
    {
        try {
            Log.WriteDebug("Sending results to " + user.GetIP().substring(1) + ":60603");
            Socket socket = new Socket(user.GetIP().substring(1), 60603);
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);
            
            File[] originals = job.GetFiles();
            String folder = originals[0].getParent()+ "/processed/";
            List<File> files = Files.walk(Paths.get(folder))
                                .filter(Files::isRegularFile)
                                .map(Path::toFile)
                                .collect(Collectors.toList());
            
            dos.writeUTF("result");
            dos.flush();
            dos.writeUTF(job.GetJobDescription());
            dos.flush();
            dos.writeUTF(job.GetID().toString());
            dos.flush();
            dos.writeInt(job.GetWorkloadFactor());
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
        } catch (IOException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
