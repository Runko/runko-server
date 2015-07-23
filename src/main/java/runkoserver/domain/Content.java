
package runkoserver.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    private String name;

    @ManyToOne
    private Person owner;

    @ManyToMany
    private List<Person> subscribers;

    protected Content() {
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return id;
    }

}

