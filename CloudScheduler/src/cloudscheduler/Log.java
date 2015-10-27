/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

import org.opennebula.client.OneResponse;

/**
 *
 * @author Pieter
 */
public class Log {
    /*
    Log Levels:
    0. Errors only
    1. Warnings as well
    2. Info as well
    3. Debug as well
    */
    public static int LogLevel = 3;
    public static String LogFile = "";
    
    public static void WriteDebug(String str)
    {
        if(LogLevel >= 3)
        {
            WriteLog("Debug: "+str);   
        }
    }
    
    public static void WriteError(String str)
    {
        if(LogLevel >= 0)
        {
            WriteLog("Error: "+str);   
        }
    }
        
    public static void WriteWarning(String str)
    {
        if(LogLevel >= 1)
        {
            WriteLog("Warning: "+str);   
        }
    }
            
    public static void WriteInfo(String str)
    {
        if(LogLevel >= 2)
        {
            WriteLog("Info: "+str);   
        }
    }
    
    public static void WriteOneResponse(OneResponse response)
    {        
        if(response.isError())
        {
            Log.WriteError(response.getErrorMessage());                
        }
        else                
        {
            Log.WriteDebug(response.getMessage());
        }
    }
    
    private static void WriteLog(String str)
    {
        // Write to standardout if no file is specified
        if("".equals(Log.LogFile))
        {
            System.out.println(str);             
        }
        else            
        {
            // Write to file
        }
    }
}
