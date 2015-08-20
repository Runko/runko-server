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
    
    //TODO subscriptions
    
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
    
    public String getVisibilityText() {
        if (getVisibility() == false) {
            return MESSAGE_PRIVATE_ELEMENT;
        }
        return MESSAGE_PUBLIC_ELEMENT;
    }
}
