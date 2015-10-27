/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.util.Date;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Pieter
 */
public class Scheduler {
    
    Client myClient;
    
    public Scheduler(Client oneClient)
    {
        myClient = oneClient;
    }
    
    public void TestSequence() throws InterruptedException
    {
        VirtualMachineWrapper vm = CreateNewVirtualMachine();
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
        }
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
