/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 * @author Pieter
 */
public class Scheduler implements JobScheduler {
    
    Client myClient;
    List<VirtualMachineWrapper> machines;
    Map usersJobs;
    Thread monitorThread;
    boolean stopLogging;
    int logInterval;
    
    public Scheduler(Client oneClient)
    {
        myClient = oneClient;
        machines = new ArrayList<>();
        usersJobs = new HashMap();
        
        stopLogging = false;
        logInterval = 2500;
        StartMonitoring();
    }
    
    private void StartMonitoring()
    {
        monitorThread = new Thread(new Runnable()
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
        
        monitorThread.start();
    }
    
    private void LogMonitorInformation()
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
    }
    
    public void TestSequence() throws InterruptedException
    {
        ScheduleJob(new Job(""), new User("",""));
        /*VirtualMachineWrapper vm = CreateNewVirtualMachine();
        if(vm != null)
        {
            int cpu = 0 ;
            while(cpu < 20)
            {
                Date now = new Date();
                Log.WriteDebug(now.toString() +": Waiting for CPU load, current " + cpu);
                Thread.sleep(60000);
                cpu = vm.GetCPU();
            }
            
            VirtualMachineWrapper vm2 = CreateNewVirtualMachine();
        }*/
    }
    
    @Override
    public void ScheduleJob(Job job, User user)
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
    }
    
    private VirtualMachineWrapper CreateNewVirtualMachine()
    {       
        VirtualMachineWrapper vm_wrapped = null;
        Log.WriteDebug("Attempting to create a VM...");
        String template = Template.Qcow();
        OneResponse vmCreateResponse = VirtualMachine.allocate(myClient, template);
        Log.WriteOneResponse(vmCreateResponse);
        
        if(!vmCreateResponse.isError())
        {
            Log.WriteInfo("Succesfully created VM with id " + vmCreateResponse.getMessage());
            int vm_id = vmCreateResponse.getIntMessage();
            VirtualMachine vm = new VirtualMachine(vm_id, myClient);
            vm_wrapped = new VirtualMachineWrapper(vm);
        }
        
        return vm_wrapped;
    }
    
    public void Close()
    {
        Log.WriteInfo("Scheduler.Close() called - stop monitoring and release all VMs");
        stopLogging = true; // This will make sure the monitoring thread stops
        
        for(int i =0; i<machines.size(); i++)
        {
           machines.get(i).Shutdown();
        }
    }
}
