package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.repository.ElementRepository;

/**
 * Service for Elements' interactions
 */
@Service
public class ElementService {

    @Autowired
    ElementRepository elementReposiory;

    public boolean saveElement(Element element) {
        if (element != null) {            
            elementReposiory.save(element);
            return true;
        }
        return false;
    }

    public boolean deleteElement(Long id, Person loggedPerson) {
        if (elementReposiory.exists(id)) {
            Element element = elementReposiory.findOne(id);
            if (element.getOwner().getId() == loggedPerson.getId()) {
                //TODO remowe element-area connection
                elementReposiory.delete(id);
                return true;
            }
        }
        return false;
    }

    public Element findById(Long id) {
        return elementReposiory.findOne(id);
    }

    public List<Element> findByName(String name) {
        return elementReposiory.findByName(name);
    }

    public List<Element> findAll() {
        return elementReposiory.findAll();
    }

    //TODO add areaIds
    public Content createContent(String name, String textArea, Person owner, List<Area> areas) {
        Content content = new Content();
        content.setName(name);
        content.setElement(textArea);
        content.setOwner(owner);
        content.setCreationTime();
        content.setAreas(areas);
        return content;
    }
}
