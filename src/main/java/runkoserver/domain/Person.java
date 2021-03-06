package runkoserver.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Person objects are used to save user information.
 */
@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String username;

    private String password;

    private String urlToPhoto;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "subscribers")
    private List<Area> subscriptions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Area> ownedAreas;

    @OneToMany
    private List<Content> ownedContents;
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "bookmarkers")
    private List<Content> bookmarks;
    
    protected Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrlToPhoto(String urlToPhoto) {
        this.urlToPhoto = urlToPhoto;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Area> getSubscriptions() {
        if (null == subscriptions) {
            subscriptions = new ArrayList<Area>();
        }
        return subscriptions;
    }

    public void setSubscriptions(List<Area> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
    public void setBookmarks(List<Content> bookmarks){
        this.bookmarks = bookmarks;
    }
    
    public List<Content> getBookmarks(){
        if (null == bookmarks){
            bookmarks = new ArrayList<Content>();
        }
        return bookmarks;
    }

    public List<Area> getOwnedAreas() {
        return ownedAreas;
    }

    public void setOwnedAreas(List<Area> ownedAreas) {
        this.ownedAreas = ownedAreas;
    }

    public List<Content> getOwnedContents() {
        return ownedContents;
    }

    public void setOwnedContents(List<Content> ownedContents) {
        this.ownedContents = ownedContents;
    }

    @Override
    public String toString() {
        return String.format("Person[id=%d, name='%s']",
                id, name);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
