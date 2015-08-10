package runkoserver.domain.content;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 * A FancyContent element that contains the URL to a picture.
 */
@Entity
public class PictureUrlElement extends Element implements Serializable{
    
    private String url;
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getUrl(){
        return this.url;
    }
}
