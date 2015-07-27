package runkoserver.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Area {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Person owner;

    @ManyToMany
    private List<Person> subscribers;

    @ManyToMany
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

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

}
