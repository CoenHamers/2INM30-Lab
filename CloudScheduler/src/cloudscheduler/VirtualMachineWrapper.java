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
    
    private void UpdateInfoFields()
    {        
        OneResponse info = wrappee.info();
        String xmlResponse = info.getMessage();
        String cpu = XmlParser.ExtractElement(xmlResponse, "CPU");
        lastKnownCPU = Integer.parseInt(cpu);
        String memory = XmlParser.ExtractElement(xmlResponse, "MEMORY");
        lastKnownCPU = Integer.parseInt(cpu);
    }
    
}
