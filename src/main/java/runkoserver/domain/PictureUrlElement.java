package runkoserver.domain;

import javax.persistence.Entity;

/**
 * A FancyContent element that contains the URL to a picture.
 */
@Entity
public class PictureUrlElement extends Element {
    private String url;

    public void setTextArea(String url){
        this.url = url;
    }
    
    public String getTextArea(){
        return this.url;
    }
}
