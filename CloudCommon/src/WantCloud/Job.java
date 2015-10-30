/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WantCloud;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @author Pieter
 */
public class Job {
    
    private boolean isRequest;
    private UUID id;
    private String job;
    private Date timeRequested;
    private File[] files;
    private int workloadFactor;
    
    public UUID GetID(){
        return id;
    }
    
    public Date GetRequestTime()
    {
        return timeRequested;
    }
    
    public String GetJobDescription()
    {
        return job;
    }
    
    public File[] GetFiles(){
        return files;
    }
    
    public boolean IsRequest()
    {
        return isRequest;
    }
    
    public int GetWorkloadFactor()
    {
        return workloadFactor;
    }
    
    public Job(UUID id, String jobDescription, File[] files, boolean isRequest, int workloadFactor)
    {
        this.id = id;
        this.job = jobDescription;
        this.timeRequested = new Date();
        this.files = files;
        this.isRequest = isRequest; // if not a request, it is a result
        this.workloadFactor = workloadFactor;
    }
}
