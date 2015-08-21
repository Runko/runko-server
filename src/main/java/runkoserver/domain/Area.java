package runkoserver.domain;

import java.util.ArrayList;
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
import static runkoserver.libraries.Messages.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Person> subscribers;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "areas")
    private List<Element> elements;

    public String getName() {
        return name;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public Person getOwner() {
        return owner;
    }

    public List<Element> getElements() {
        return elements;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void setSubscribers(List<Person> subscribers) {
        this.subscribers = subscribers;
    }

    public List<Person> getSubscribers() {
        if (this.subscribers == null) {
            setSubscribers(new ArrayList<>());
        }
        return this.subscribers;
    }

    /**
     * Add connection between Area and subscriber
     * @param subscriber subscriber to be added
     * @return true if subscriber was added
     */
    public boolean addSubscriber(Person subscriber) {
        if (subscriber != null) {
            getSubscribers().add(subscriber);
            return true;
        }
        return false;
    }
    
    /**
     * Removes connection between Area and subscriber.
     * @param subscriber subscriber to be removed
     * @return returns false if person is null or not found in subscribers.
     */
    public boolean removeSubscriber(Person subscriber) {
        if (subscriber != null) {
            if (getSubscribers().contains(subscriber)) {
                getSubscribers().remove(subscriber);
                return true;
            }
        }
        return false;
    }

    public String getVisibilityText() {
        if (getVisibility() == false) {
            return MESSAGE_PRIVATE_ELEMENT;
        }
        return MESSAGE_PUBLIC_ELEMENT;
    }
}
