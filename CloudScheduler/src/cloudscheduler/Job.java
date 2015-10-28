/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import java.util.Date;

/**
 * @author Pieter
 */
public class Job {
    
    private String job;
    private Date timeRequested;
    
    public Date GetRequestTime()
    {
        return timeRequested;
    }
    
    public String GetJobDescription()
    {
        return job;
    }
    
    public Job(String jobDescription)
    {
        this.job = jobDescription;
        this.timeRequested = new Date();
    }
}
