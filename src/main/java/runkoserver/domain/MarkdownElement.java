package runkoserver.domain;

import javax.persistence.Entity;

/**
 * A FancyContent element that contains Markdown-formatted text.
 */
@Entity
public class MarkdownElement extends Element{
    private String textArea;

    public void setTextArea(String textArea){
        this.textArea = textArea;
    }
    
    public String getTextArea(){
        return this.textArea;
    }
}
