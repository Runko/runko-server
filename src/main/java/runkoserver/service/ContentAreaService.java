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
public class ContentAreaService {
 
    @Autowired
    ElementRepository elementRepository;

    @Autowired
    AreaRepository areaRepository;

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
            saveContentToAreas(element);
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
                deleteElementFromAreas(element);
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
            for (Area area : findListedAreasById(areaIds)) {
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
            deleteElementFromAreas(content);
            content.setAreas(new ArrayList<>());
            content.setModifyTime();
            if (areaIds != null) {
                for (Area area : findListedAreasById(areaIds)) {
                    content.addArea(area);
                }
            }
            elementRepository.save(content);
            return true;
        }
        return false;
    }
//    
//    /**
//     * Creation of fancy-context.
//     *
//     * @param name name of the context
//     * @param textElement
//     * @param areaIds Id-numbers of the added areas
//     * @param owner creator of content
//     * @return created FancyContent
//     */
//    public Content createFancyContent(String name, String textElement, List<Long> areaIds, Person owner) {
//        Content content = new Content();
//        
//        content.setName(name);
//        content.setOwner(owner);
//        content.setCreationTime();
//        
//        content.setAreas(new ArrayList<>());
//        if (areaIds != null) {
//            for (Area area : findListedAreasById(areaIds)) {
//                content.addArea(area);
//            }
//        }
//        
//        content.setElements(new ArrayList<>());
//        
//        TextElement text = new TextElement();
//        text.setTextArea(textElement);
//        content.getElements().add(text);
//
//        return content;
//    }

    //Areas' repository interactions
    public boolean saveArea(Area area) {
        if (area != null) {
            areaRepository.save(area);
            return true;
        }
        return false;
    }

    public List<Area> findAllAreas() {
        return areaRepository.findAll();
    }

    public List<Area> findAllPublicAreas() {
        return areaRepository.findByVisibilityTrue();
    }

    public Area findAreaById(Long id) {
        return areaRepository.findOne(id);
    }

    public Area findAreaByName(String name) {
        return areaRepository.findByName(name);
    }

    /**
     * Creates a new area. Does NOT save the area to repository.
     *
     * @param name name of the area
     * @param person owner of the area
     * @param visibility is area visible for unauthenticated visitors
     * @return created Area
     */
    public Area createArea(String name, Person person, Boolean visibility) {
        Area area = new Area();
        area.setName(name);
        area.setOwner(person);
        area.setVisibility(visibility);

        return area;
    }

    /**
     * Finds all areas by id from the given list by their id and returns them as
     * Area-objects. Used by CreateSimpleContent-method
     *
     * @param areaIds Areas' ids
     * @return list of Area-objects
     */
    private List<Area> findListedAreasById(List<Long> areaIds) {
        List<Area> areas = new ArrayList<>();
        for (Long id : areaIds) {
            areas.add(findAreaById(id));
        }
        return areas;
    }

    /**
     * Adds connection between given content and it's Areas. Used by delete
     * content.
     *
     * @param content on which the connections are wanted
     */
    private void saveContentToAreas(Element element) {
        for (Area area : element.getAreas()) {
            
            if (!area.getElements().contains(element)){
                area.addElements(element);
            }    
            areaRepository.save(area);
        }
    }

    /**
     * Deletes connection between given connection and it's Areas-
     *
     * @param content on which connections are disabled
     */
    private void deleteElementFromAreas(Element element) {
        for (Area area : element.getAreas()) {
            area.deleteElement(element);
            areaRepository.save(area);
        }
    }

    //shared interactions
    /**
     * Deletion ONLY for testing purposes
     *
     * @return was repositories emptied.
     */
    public boolean deleteAll() {
        elementRepository.deleteAll();
        areaRepository.deleteAll();
        if (findAllAreas().isEmpty() && findAllElements().isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Element> findByOwner(Person person) {
        return elementRepository.findByOwner(person);
    }

    void addSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.add(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
    }

    void deleteSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.remove(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
    }
    
    void addBookmarks(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.add(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    }
    
    void deleteBookmarks(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.remove(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    }
    
    /**
     * 
     * @param person tells whose frontpage must be build
     * @return the content in chronological order
     */
    public List<Content> createListFromSubscripedContents(Person person) {
        List<Content> newList = new ArrayList<>();
        
        // Builds new list without dublicates
        person.getSubscriptions().stream().forEach((area) -> {
            area.getElements().stream().filter((content)
                    -> (!newList.contains(content))).forEach((content) -> {
                        newList.add((Content) content);
                    });
        });
        // sorts new list to chronological order. Newest first.
        newList.sort((Content x, Content y) -> {
            return y.getModifyTime().compareTo(x.getModifyTime());
        });
        
        return newList;
    }
    
}
