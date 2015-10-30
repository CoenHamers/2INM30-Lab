/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import WantCloud.JobProcessor;
import WantCloud.User;
import WantCloud.Log;
import WantCloud.Job;
import WantCloud.JobListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 * @author Pieter
 */
public class Scheduler implements JobProcessor {
    
    Client myClient;
    List<VirtualMachineWrapper> machines;
    Map usersJobs;
    Map machineJob;
    Map jobUpdate;
    int logInterval;
    Thread listeningThread;
    boolean stopThread;
    ServerSocket ssocket;
    
    public Scheduler(Client oneClient)
    {
        myClient = oneClient;
        machines = new ArrayList<>();
        usersJobs = new HashMap();
        machineJob = new HashMap();
        jobUpdate = new HashMap();
        
        logInterval = 2500;
        StartMonitoring();
        EnsureReliability();
    }
    
    private VirtualMachineWrapper VmForIp(String ip)
    {
        for(int i =0; i<machines.size(); i++)
            {
                String machineip = machines.get(i).GetIP();
                Log.WriteInfo("Matching ips: " + machineip + ", " + ip.substring(1) + "= "+ ip.substring(1).equals(machineip));
                if(ip.substring(1).equals(machineip))
                    return machines.get(i);
            }
        return null;
    }
    
    private void EnsureReliability()
    {
         Thread reliabilityThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(!stopThread)
                {
                    try {
                        Thread.sleep(1000);
                        Set jobs = jobUpdate.keySet();
                        for(Object job : jobs)
                        {
                            Log.WriteDebug("Making sure machines are still alive");
                            //Job job = (Job)j;
                            Date lastUpdate = (Date)jobUpdate.get(job);
                            Date date = new Date();
                            
                            long diff = date.getTime() - lastUpdate.getTime();
                            if(diff > 10000)
                            {
                                Log.WriteDebug("Did not receive an update in more than 10 seconds! " + diff);
                            }
                            
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
          
        reliabilityThread.start();
    }
    
    private void StartMonitoring()
    {
         try {
            Log.WriteDebug("Start istening for monitor on port " + 60607);
            ssocket = new ServerSocket(60607);
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
                    Log.WriteDebug("Started listening to incoming performance.");
                    try {
                        Socket socket = ssocket.accept();
                        
                        // TODO: Run this in a separate thread again, so new clients can immediately be responded to
                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                        DataInputStream dis = new DataInputStream(bis);
                        String vmid = dis.readUTF();
                        String cpu = dis.readUTF();
                        String memoryTotal = dis.readUTF();
                        String memoryCurrent = dis.readUTF();
                        String memorySwapTotal = dis.readUTF();
                        String memorySwapUsed = dis.readUTF();
                        

                        String ip = socket.getInetAddress().toString();
                        VirtualMachineWrapper vm = VmForIp(ip);
                        if(vm != null)
                        {
                            vmid = vm.GetID();
                        }
                        MonitorResult m = new MonitorResult(vmid, cpu, memoryTotal, memoryCurrent, memorySwapTotal, memorySwapUsed);
                        
                        Job jobForMachine = (Job)machineJob.get(vm);
                        jobUpdate.put(jobForMachine, new Date());
                        Log.WriteDebug("Monitor: " + m.AsString());
                        
                        dis.close();
                        bis.close();
                        socket.close();    
                        
                        Date now = new Date();
                        try {
                            PrintWriter out = new PrintWriter(new FileOutputStream(new File("PerformanceLog.txt"),  true));
                            out.println(now.toString() + ";" + m.AsString());         

                            out.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
        
        /*monitorThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!stopLogging)
                {
                    LogMonitorInformation();     
                    try {
                        Thread.sleep(logInterval);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        monitorThread.start();*/
    }
    
    /*private void LogMonitorInformation()
    {
        // Don't even want to open the file if no machines are connected
        if(machines.size() == 0)
            return;
        
        Date now = new Date();
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(new File("PerformanceLog.txt"),  true));
            
            for(int i =0; i<machines.size(); i++)
            {
                String monitorSummary = machines.get(i).GetMonitorSummary();   
                out.println(now.toString() + ";" + monitorSummary);         
            }
            
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    @Override
    public void ProcessJob(Job job, User user)
    {
        if(job.IsRequest())
        {
            ScheduleJob(job, user);
        }
        else            
        {
            Log.WriteDebug("Received job results for job " + job.GetID());
        }
       
    }
    
    private void ScheduleJob(Job job, User user)
    {
         usersJobs.put(user.GetID(), job);
        
        VirtualMachineWrapper vm = CreateNewVirtualMachine();
        machines.add(vm);
        boolean vmBootDone = false;
        
        while(!vmBootDone)
        {
            String vmState = vm.GetState();
            vmBootDone = (vmState.equals("Active"));
            Log.WriteDebug("Waiting for VM " + vm.GetID() + " to boot, current status " + vmState);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        vm.AssignJob(job);
        machineJob.put(vm, job);
    }
    
    private VirtualMachineWrapper CreateNewVirtualMachine()
    {       
        VirtualMachineWrapper vm_wrapped = null;
        //Log.WriteDebug("Attempting to create a VM...");
        //String template = Template.Qcow();
        //OneResponse vmCreateResponse = VirtualMachine.allocate(myClient, template);
        //Log.WriteOneResponse(vmCreateResponse);
        
        //if(!vmCreateResponse.isError())
        //{
            //Log.WriteInfo("Succesfully created VM with id " + vmCreateResponse.getMessage());
            int vm_id = 41016;//vmCreateResponse.getIntMessage();
            VirtualMachine vm = new VirtualMachine(vm_id, myClient);
            vm_wrapped = new VirtualMachineWrapper(vm);
        //}
        
        return vm_wrapped;
    }
    
    public void Close()
    {
        Log.WriteInfo("Scheduler.Close() called - stop monitoring and release all VMs");
        stopThread = true; // This will make sure the monitoring thread stops
        
        for(int i =0; i<machines.size(); i++)
        {
           //machines.get(i).Shutdown();
        }
    }
}
