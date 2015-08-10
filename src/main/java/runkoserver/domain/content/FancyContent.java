package runkoserver.domain.content;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class FancyContent extends Content implements Serializable {

    @OneToMany(mappedBy = "fancyContent", fetch=FetchType.EAGER)
    private ArrayList<Element> elements; //vai hashmap?

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }
}
