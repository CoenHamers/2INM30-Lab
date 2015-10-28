/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

/**
 * @author Pieter
 */
public interface JobScheduler {    
    public void ScheduleJob(Job job, User user);     
}
