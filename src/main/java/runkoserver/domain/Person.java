package runkoserver.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Runko's user information based on Kuksa.
 */
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String username;

    private String urlToPhoto;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "subscribers")
    private List<Area> subscriptions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Element> ownElements;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Area> ownAreas;

    protected Person() {
    }

    public Person(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public String getUrlToPhoto() {
        return urlToPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrlToPhoto(String urlToPhoto) {
        this.urlToPhoto = urlToPhoto;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubscriptions(List<Area> subscriptions) {
        this.subscriptions = new ArrayList<>();
    }

    public List<Element> getOwnContent() {
        if (this.ownElements == null) {
            ownElements = new ArrayList<>();
        }
        return ownElements;
    }

    public List<Area> getSubscriptions() {
        if (subscriptions == null) {
            setSubscriptions(new ArrayList<>());
        }
        return subscriptions;
    }

    /**
     * Add connection between Person and Subscribption
     * @param subscription are to be subscibed
     * @return true if subscription was added
     */
    public boolean addSubscription(Area subscription) {
        if (subscription != null) {
            getSubscriptions().add(subscription);
            return true;
        }
        return false;
    }

    public void setOwnContent(List<Element> ownElements) {
        this.ownElements = ownElements;
    }
}
