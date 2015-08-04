package runkoserver.domain;

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

@Entity
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank
    @Length(min = 4, max = 50)
    private String name;
    
    private boolean visibility;

    @ManyToOne
    private Person owner;

    @ManyToMany
    private List<Person> subscribers;

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Content> contents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Person> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Person> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean addContent(Content content) {
        if (!contents.contains(content)) {
            contents.add(content);
            return true;
        }
        return false;
    }
    
    public boolean deleteContent(Content content) {
        if (contents.contains(content)) {
            contents.remove(content);
            return true;
        }
        return false;
    }
    
    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;

    }

    public boolean getVisibility() {
        return visibility;
    }
}
