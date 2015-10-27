/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Pieter
 */
public class Scheduler {
    
    Client myClient;
    List<VirtualMachineWrapper> machines;
    
    public Scheduler(Client oneClient)
    {
        myClient = oneClient;
        machines = new ArrayList<VirtualMachineWrapper>();
    }
    
    public void TestSequence() throws InterruptedException
    {
        ScheduleJob("");
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
    
    public void ScheduleJob(String job)
    {
        VirtualMachineWrapper vm = CreateNewVirtualMachine();
        machines.add(vm);
        boolean vmBootDone = false;
        
        while(!vmBootDone)
        {
            String vmState = vm.GetState();
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
    
}
