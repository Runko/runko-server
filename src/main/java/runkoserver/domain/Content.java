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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Upper class for all content types.
 */
@Entity
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    
    @NotBlank
    @Length(min = 3, max = 50)
    private String name;

    @ManyToOne
    private Person owner;

    @ManyToMany
    private List<Area> areas;

    protected Content() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public boolean addArea(Area area) {
        if (!areas.contains(area)) {
            this.areas.add(area);
            return true;
        }
        return false;
    }

    public boolean deleteArea(Area area) {
        if (areas.contains(area)) {
            areas.remove(area);
            return true;
        }
        return false;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
