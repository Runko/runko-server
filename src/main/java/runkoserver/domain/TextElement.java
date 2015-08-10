package runkoserver.domain;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 * A FancyContent element that contains text.
 */
@Entity
public class TextElement extends Element implements Serializable{
    
    private String textArea; //tai miten lie sit markdownattu teksti kandee tallentaa
    
    public void setTextArea(String textArea){
        this.textArea = textArea;
    }
    
    public String getTextArea(){
        return this.textArea;
    }
}