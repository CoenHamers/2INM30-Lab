/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

/**
 *
 * @author Pieter
 */
public class MonitorResult {
    private String vmid;
    private String cpu;
    private String memoryTotal;
    private String memoryCurrent;
    private String memorySwapTotal;
    private String memorySwapUsed;
    
    public MonitorResult(String vmid, String cpu, String memtot, String memcur, String memstot, String memsused)
    {
        this.vmid = vmid;
        this.cpu = cpu;
        this.memoryTotal = memtot;
        this.memoryCurrent = memcur;
        this.memorySwapTotal = memstot;
        this.memorySwapUsed = memsused;
    }
    
    public String AsString()
    {
        return vmid + ";" + cpu + ";" + memoryTotal+ ";" +memoryCurrent+ ";" +memorySwapTotal+ ";" +memorySwapUsed; 
    }
}
