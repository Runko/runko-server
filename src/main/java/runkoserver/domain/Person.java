package runkoserver.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Runko's user information based on Kuksa.
 */

@Entity
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String username;
    
    private String urlToPhoto;
    
    private String description;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Element> ownElements;;
    
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
    
    public List<Element> getOwnContent() {
        if (this.ownElements == null) {
            ownElements = new ArrayList<>();
        }
        return ownElements;
    }
    
    public void setOwnContent(List<Element> ownElements) {
        this.ownElements = ownElements;
    }
}
