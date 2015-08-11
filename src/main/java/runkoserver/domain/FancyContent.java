package runkoserver.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class FancyContent extends Content implements Serializable {

    @OneToMany(mappedBy = "fancyContent", fetch=FetchType.EAGER)
    private List<Element> elements; //vai hashmap?

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
