package runkoserver.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
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
public class Area implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Length(min = 4, max = 50)
    private String name;

    private boolean visibility;

    @ManyToOne
    private Person owner;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Person> subscribers;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "areas")
    private List<Element> elements;

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
        if (subscribers == null) {
            subscribers = new ArrayList<>();
        }

        return subscribers;
    }

    public void setSubscribers(List<Person> subscribers) {
        this.subscribers = subscribers;
    }

    /**
     * Adds the given Element to the Area if it is not already there.
     *
     * @param element the element to be added
     * @return true if the Area did not already contain the element, false
     * if it did
     */
    public boolean addElement(Element element) {
        if (!elements.contains(element)) {
            elements.add(element);
            return true;
        }

        return false;
    }

    /**
     * Deletes an element from the Area.
     *
     * @param element the element to be deleted
     * @return true if the area contained the element, false if not
     */
    public boolean deleteElement(Element element) {
        if (elements.contains(element)) {
            elements.remove(element);
            return true;
        }
        return false;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;

    }

    public boolean getVisibility() {
        return visibility;
    }

    /**
     * Returns a String that describes the visibility status of the Area.
     *
     * @return "Kirjautuneille" if the Area is private, "Julkinen" if public
     */
    public String getVisibilityText() {
        if (getVisibility() == false) {
            return "Kirjautuneille";
        }
        return "Julkinen";
    }

    /**
     * Makes sure the Area's element list does not contain any duplicates.
     */
    public void cleanElementList() {
        ArrayList<Element> noDoubles = new ArrayList<>();
        for (Element ele : elements) {
            if (!noDoubles.contains(ele)) {
                noDoubles.add(ele);
            }
        }
        this.elements = noDoubles;
    }
}
