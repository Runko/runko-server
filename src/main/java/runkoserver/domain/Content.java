
package runkoserver.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Element called content.
 */
@Entity
public class Content extends Element implements Serializable{

    @Lob
    private String textArea;
    
    @Override
    public String getElement() {
        return this.textArea;
    }

    @Override
    public void setElement(String element) {
        this.textArea = element;
    }
    
}
