package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.Person;
import runkoserver.domain.SimpleContent;
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
    public boolean deleteContent(Long id) {
        if (contentRepository.exists(id)) {
            Content content = contentRepository.findOne(id);
            deleteContentFromAreas(content);
            contentRepository.delete(content.getId());
            return true;
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
        if (areaIds != null) {
            for (Area area : findListedAreasById(areaIds)) {
                content.addArea(area);
            }
        }

        return content;
    }
 
    public SimpleContent updateSimpleContent(Long contentId, String name, String textArea, List<Long> areaIds) {
        SimpleContent content = (SimpleContent) findContentById(contentId);
        content.setName(name);
        content.setTextArea(textArea);
        deleteContentFromAreas(content);
        content.setAreas(new ArrayList<>());
        if (areaIds != null) {
            for (Area area : findListedAreasById(areaIds)) {
                content.addArea(area);
            }
        }
        contentRepository.save(content);
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
            area.addContent(content);

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
    public List<Content> findByOwner(Person person){
    return contentRepository.findByOwner(person);
    }

}
