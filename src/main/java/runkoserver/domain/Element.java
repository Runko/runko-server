package runkoserver.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Superclass for different elements that can be saved to Runko.
 */
@Entity
public abstract class Element implements Serializable {
    
    @Id
    @GeneratedValue
    private long id;
    
    @NotBlank
    @Length(min = 3, max = 50)
    private String name;
  
    @ManyToOne
    private Person owner;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Area> areas;
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean hasPublicAreas() {
        for (Area area : areas) {
            if (area.getVisibility()) {
                return true;
            }
        }
        return false;
    }
}
