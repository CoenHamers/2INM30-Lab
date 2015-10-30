/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WantCloud;

import WantCloud.User;
import WantCloud.Job;

/**
 * @author Pieter
 */
public interface JobProcessor {    
    public void ProcessJob(Job job, User user);     
}
