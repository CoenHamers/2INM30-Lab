/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

/**
 * @author Pieter
 */
public class User {
    private String ip;
    private String id;
    
    public String GetIP()
    {
        return ip;
    }
    
    public String GetID()
    {
        return id;
    }
    
    public User(String ip, String id)
    {
        this.ip = ip;
        this.id = id;
    }
}
