
package runkoserver.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Element called content.
 */
@Entity
public class Content extends Element implements Serializable{

    /**
     * Content's element is a text-area
     */
    @Lob
    private String element;
    
    @Override
    public String getElement() {
        return this.element;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }
    
}
