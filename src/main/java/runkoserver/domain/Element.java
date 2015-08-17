package runkoserver.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Superclass for different elements that a FancyContent can contain.
 */
@Entity
public abstract class Element implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    private Content content;
    
    public void setContent(Content content) {
     this.content = content;
    }
     public void getContent(Content content) {
     this.content = content;
    }
    private String text;
    public long getId() {
        return id;
    }
    public String getText(){
    return text;
    }
}
