package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.*;
import runkoserver.repository.AreaRepository;
import runkoserver.repository.ElementRepository;

/**
 * Class for repository-interactions of Areas and Content.
 */
@Service
public class ElementService {
 
    @Autowired
    ElementRepository elementRepository;

    @Autowired
    AreaRepository areaRepository;
    
    @Autowired
    AreaService areaService;

    //Contents' repository interactions.
    /**
     * Saves new content and adds connections to corresponding areas.
     *
     * @param element
     * @return was save successful
     */
    public boolean saveElement(Element element) {
        if (element != null) {
            elementRepository.save(element);
            areaService.saveContentToAreas(element);
            return true;
        }
        return false;
    }

    public List<Element> findAllElements() {
        return elementRepository.findAll();
    }

    public Element findElementById(Long id) {
        return elementRepository.findOne(id);
    }

    public Element findElementByName(String name) {
        return elementRepository.findByName(name);
    }

    /**
     * Deletes content and removes any connections with its areas.
     *
     * @param id content id
     * @param whoIsLoggedIn current logged user
     * @return was delete successful
     */
    public boolean deleteElement(Long id, Person whoIsLoggedIn) {
        
        if (elementRepository.exists(id)) {

            Element element = elementRepository.findOne(id);
            if (element.getOwner().getId() == whoIsLoggedIn.getId()) {
                areaService.deleteElementFromAreas(element);
                elementRepository.delete(element.getId());
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Creation of context. Does NOT save created content to repository.
     *
     * @param name name of the context
     * @param textArea text area of the context
     * @param areaIds Id-numbers of the added areas
     * @param owner creator of content
     * @return created SimpleContent
     */
    public Content createContent(String name, String textArea, List<Long> areaIds, Person owner) {
        Content content = new Content();
        content.setAreas(new ArrayList<>());
        content.setName(name);
        content.setTextArea(textArea);
        content.setOwner(owner);
        content.setCreationTime();
        if (areaIds != null) {
            for (Area area : areaService.findListedAreasById(areaIds)) {
                content.addArea(area);                
            }
        }

        return content;
    }

    public boolean updateContent(Long elementId, String name, String textArea, List<Long> areaIds, Person whoIsLogged) {
        Content content =  (Content)findElementById(elementId);
        if (whoIsLogged.getId() == content.getOwner().getId()) {
            content.setName(name);
            content.setTextArea(textArea);
            areaService.deleteElementFromAreas(content);
            content.setAreas(new ArrayList<>());
            content.setModifyTime();
            if (areaIds != null) {
                for (Area area : areaService.findListedAreasById(areaIds)) {
                    content.addArea(area);
                }
            }
            elementRepository.save(content);
            return true;
        }
        return false;
    }

    //shared interactions
    /**
     * Deletion ONLY for testing purposes
     *
     * @return was repositories emptied.
     */
    public boolean deleteAllElements() {
        elementRepository.deleteAll();
        if (findAllElements().isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Element> findElementsByOwner(Person person) {
        return elementRepository.findByOwner(person);
    }
       
    public void addBookmarks(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.add(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    }
    
    public void deleteBookmarks(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.remove(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    } 
}
