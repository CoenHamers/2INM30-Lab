/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Pieter
 */
public class VirtualMachineWrapper {
    
    // Wrappee is the VM being wrapped :D
    VirtualMachine wrappee;
    int lastKnownCPU = 0;
    int lastKnownMemory = 0;
    String ip = "";
    String state = "";
    String id = "";
    boolean shutdown = false;
    
    public int GetCPU()
    {
        UpdateInfoFields();
        return lastKnownCPU;
    }
    
    public int GetMemoryKb()
    {
        UpdateInfoFields();
        return lastKnownCPU;        
    }
    
    public String GetState()
    {
        UpdateInfoFields();
        return state;
    }
    
    public String GetID()
    {
        if("".equals(ip))
        {
            OneResponse info = wrappee.info();
            String idResult = XmlParser.ExtractElement(info.getMessage(), "ID");
            //<IP><![CDATA[10.141.3.171]]></IP> - there can be better parsing (for variable ip adress lengths)
            id = idResult;
        }
        return ip;        
    }
    
    public String GetIP()
    {
        if("".equals(ip))
        {
            OneResponse info = wrappee.info();
            String ipresult = XmlParser.ExtractElement(info.getMessage(), "IP");
            //<IP><![CDATA[10.141.3.171]]></IP> - there can be better parsing (for variable ip adress lengths)
            ip = ipresult.substring(9, 21);
        }
        return ip;
    }
    
    public VirtualMachineWrapper(VirtualMachine machineToWrap)
    {
        wrappee = machineToWrap;
    }
    
    public void AssignJob(Job job)
    {
        if(shutdown)
        {
            Log.WriteWarning("Invoked AssignJob on "+id+" which is being shutdown - will not assign job");
            return;            
        }
        
        Log.WriteDebug(id + "received job " + job.GetJobDescription());
        //Log.WriteDebug("Instead of assigning a job, as a place holder we just shut down the VM.");
        //wrappee.shutdown();
    }
    
    public void Shutdown()
    {        
        Log.WriteDebug("Shutting down VM " + id);
        shutdown = true; // prevents other methods from being invoked on the VM while it is no longer active
        wrappee.shutdown();
    }
    
    private void UpdateInfoFields()
    {        
        if(shutdown)
        {
            Log.WriteWarning("Invoked UpdateInfoFields on "+id+" which is being shutdown - will not assign job");
            return;            
        }
        
        OneResponse info = wrappee.info();
        String xmlResponse = info.getMessage();
        String cpu = XmlParser.ExtractElement(xmlResponse, "CPU");
        lastKnownCPU = Integer.parseInt(cpu);
        String memory = XmlParser.ExtractElement(xmlResponse, "MEMORY");
        lastKnownCPU = Integer.parseInt(cpu);
        String stateNumber = XmlParser.ExtractElement(xmlResponse, "STATE");
        state = StateNumberToString(stateNumber);
        
        
        Log.WriteDebug("Checking State with state(): " + wrappee.state());
        Log.WriteDebug("Checking State with status(): " + wrappee.status());
        Log.WriteDebug("Checking State with stateStr(): " + wrappee.stateStr());
    }
    
    public String GetMonitorSummary()
    {
        UpdateInfoFields();
        return id + ";" + lastKnownMemory + ";" + lastKnownCPU;
    }
    // http://docs.opennebula.org/4.12/integration/system_interfaces/api.html#actions-for-virtual-machine-management
    // There are multiple methods to request the status of a vm, the methods (with example returnvalue):
    // state()          3
    // status()         runn
    // stateStr()       ACTIVE
    private String StateNumberToString(String state)            
    {
        switch(state)
        {
            case "-2": return "Any";
            case "-1": return "AnyButDone";
            case "0": return "Init";
            case "1": return "Pending";
            case "2": return "Hold";
            case "3": return "Active";
            case "4": return "Stopped";
            case "5": return "Suspended";
            case "6": return "Done";
            case "7": return "Failed";
            default:  // Not sure what to do here
                return "Any";
        }
    }
}
