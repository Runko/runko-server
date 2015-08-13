package runkoserver.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.domain.SimpleContent;
import runkoserver.domain.FancyContent;
import runkoserver.domain.TextElement;
import runkoserver.repository.AreaRepository;
import runkoserver.repository.ContentRepository;

/**
 * Class for repository-interactions of Areas and Content.
 */
@Service
public class ContentAreaService {

    @Autowired
    ContentRepository contentRepository;

    @Autowired
    AreaRepository areaRepository;

    //Contents' repository interactions.
    /**
     * Saves new content and adds connections to corresponding areas.
     *
     * @param content content to be added
     * @return was save successful
     */
    public boolean saveContent(Content content) {
        if (content != null) {
            contentRepository.save(content);
            saveContentToAreas(content);
            return true;
        }
        return false;
    }

    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    public Content findContentById(Long id) {
        return contentRepository.findOne(id);
    }

    public Content findContentByName(String name) {
        return contentRepository.findByName(name);
    }

    /**
     * Deletes content and removes any connections with its areas.
     *
     * @param id content id
     * @return was delete successful
     */
    public boolean deleteContent(Long id, Person whoIsLoggedIn) {
        if (contentRepository.exists(id)) {

            Content content = contentRepository.findOne(id);
            if (content.getOwner().getId() == whoIsLoggedIn.getId()) {
                deleteContentFromAreas(content);
                contentRepository.delete(content.getId());
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Creation of simple-context.
     *
     * @param name name of the context
     * @param textArea text area of the context
     * @param areaIds Id-numbers of the added areas
     * @param owner creator of content
     * @return created SimpleContent
     */
    public SimpleContent createSimpleContent(String name, String textArea, List<Long> areaIds, Person owner) {
        SimpleContent content = new SimpleContent();
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

    public boolean updateSimpleContent(Long contentId, String name, String textArea, List<Long> areaIds, Person whoIsLogged) {

        SimpleContent content = (SimpleContent) findContentById(contentId);
        if (whoIsLogged.getId() == content.getOwner().getId()) {
            content.setName(name);
            content.setTextArea(textArea);
            deleteContentFromAreas(content);
            content.setAreas(new ArrayList<>());
            content.setModifyTime();
            if (areaIds != null) {
                for (Area area : findListedAreasById(areaIds)) {
                    content.addArea(area);
                }
            }
            contentRepository.save(content);
            return true;
        }
        return false;
    }
    
    /**
     * Creation of fancy-context.
     *
     * @param name name of the context
     * @param textElement
     * @param areaIds Id-numbers of the added areas
     * @param owner creator of content
     * @return created FancyContent
     */
    public FancyContent createFancyContent(String name, String textElement, List<Long> areaIds, Person owner) {
        FancyContent content = new FancyContent();
        
        content.setName(name);
        content.setOwner(owner);
        content.setCreationTime();
        
        content.setAreas(new ArrayList<>());
        if (areaIds != null) {
            for (Area area : findListedAreasById(areaIds)) {
                content.addArea(area);
            }
        }
        
        content.setElements(new ArrayList<>());
        
        TextElement text = new TextElement();
        text.setTextArea(textElement);
        content.getElements().add(text);

        return content;
    }

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
     * Creates a new area.
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
    private void saveContentToAreas(Content content) {
        for (Area area : content.getAreas()) {
            
            if (!area.getContents().contains(content)){
                area.addContent(content);
            }    
            areaRepository.save(area);
        }
        
    }

    /**
     * Deletes connection between given connection and it's Areas-
     *
     * @param content on which connections are disabled
     */
    private void deleteContentFromAreas(Content content) {
        for (Area area : content.getAreas()) {
            area.deleteContent(content);
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
        contentRepository.deleteAll();
        areaRepository.deleteAll();
        if (findAllAreas().isEmpty() && findAllContent().isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Content> findByOwner(Person person) {
        return contentRepository.findByOwner(person);
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
    /**
     * 
     * @param person tells whos frontpage must be build
     * @return the content in chronological order
     */
    public List<Content> createListFromSubscripedContents(Person person) {
        List<Content> newList = new ArrayList<>();
        
        // Builds new list without dublicates
        person.getSubscriptions().stream().forEach((area) -> {
            area.getContents().stream().filter((content)
                    -> (!newList.contains(content))).forEach((content) -> {
                        newList.add(content);
                    });
        });
        // sorts new list to chronological order. Newest first.
        newList.sort((Content x, Content y) -> {
            return y.getModifyTime().compareTo(x.getModifyTime());
        });
        
        return newList;
    }

}
