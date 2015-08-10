package runkoserver.domain;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 * A FancyContent element that contains the URL to a picture.
 */
@Entity
public class PictureUrlElement extends Element implements Serializable{
    
    private String url;
    
    public void setTextArea(String url){
        this.url = url;
    }
    
    public String getTextArea(){
        return this.url;
    }
}
