package runkoserver.domain.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import runkoserver.domain.Area;
import runkoserver.domain.Person;

/**
 * Upper class for all content types.
 */
@Entity
public abstract class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @NotBlank
    @Length(min = 3, max = 50)
    private String name;

    @ManyToOne
    private Person owner;

    @ManyToMany(fetch = FetchType.EAGER)
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

    public String getCreationTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(creationTime);
    }

    public void setCreationTime() {
        this.creationTime = new Date();
        this.modifyTime = new Date();
    }

    public String getModifyTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(modifyTime);
    }

    public void setModifyTime() {
        this.modifyTime = new Date();
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
        this.areas = new ArrayList<>();
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
