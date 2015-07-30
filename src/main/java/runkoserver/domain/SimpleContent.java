package runkoserver.domain;

import javax.persistence.Entity;
/**
 * Content class for mobile publishing
 * 
 */
@Entity
public class SimpleContent extends Content{
    
    private String textArea;

    public void setTextArea(String textArea){
        this.textArea = textArea;
    }
    
    public String getTextArea(){
        return this.textArea;
    }
    
}
