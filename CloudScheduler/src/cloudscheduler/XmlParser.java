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
public class XmlParser {
    
    public static String ExtractElement(String xml, String tag)
    {
        String elementStart = "<"+tag+">";
        String elementEnd = "</"+tag+">";
        
        int startOfElement = xml.indexOf(elementStart);
        int endOfElement = xml.indexOf(elementEnd);
        if(startOfElement <0 || endOfElement <0)
            return "";
        
        return xml.substring(startOfElement + elementStart.length(), endOfElement);    
    }
}
