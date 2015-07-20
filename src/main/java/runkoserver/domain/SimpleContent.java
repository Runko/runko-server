/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.domain;

/**
 *
 * @author warva
 */

public class SimpleContent extends Content{
    
    private String textArea;

    public void setTextArea(String textArea){
        this.textArea = textArea;
    }
    
    public String getTextArea(){
        return this.textArea;
    }
    
}
