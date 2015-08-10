package runkoserver.domain.content;

import runkoserver.domain.content.FancyContent;
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
    private FancyContent fancyContent;

    public long getId() {
        return id;
    }
}
