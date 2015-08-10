package runkoserver.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Superclass for different elements that a FancyContent can contain.
 */
@Entity
public abstract class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    public Long getId() {
        return id;
    }
}
