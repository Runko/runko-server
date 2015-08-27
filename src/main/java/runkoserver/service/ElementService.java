package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.*;
import runkoserver.repository.AreaRepository;
import runkoserver.repository.ElementRepository;

/**
 * Class for repository-interactions of Elements.
 */
@Service
public class ElementService {

    @Autowired
    ElementRepository elementRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    AreaService areaService;

    /**
     * Saves an Element to the repository and adds connections to corresponding
     * areas.
     *
     * @param element the Element that will be saved
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
     * Deletes an Element and removes any connections with its areas.
     *
     * @param id element id
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
     * Creates a new Content with the given attributes. Does NOT save created
     * content to repository.
     *
     * @param name name of the content
     * @param textArea text area of the content
     * @param areaIds IDs of the Areas the content is published in
     * @param owner creator of content
     * @return the new Content object
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

    /**
     * Updates a Content element's attributes, if the currently logged in Person
     * is the owner of the Content.
     *
     * @param elementId id of the content element
     * @param name updated name for content
     * @param textArea updated textArea for content
     * @param areaIds updated list of Area IDs the content is published in
     * @param whoIsLogged the Person who is logged in
     * @return true if update was successful, false if not
     */
    public boolean updateContent(Long elementId, String name, String textArea, List<Long> areaIds, Person whoIsLogged) {
        Content content = (Content) findElementById(elementId);
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

    /**
     * Deletes all Elements from the repository.
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

    /**
     * Saves a new Content bookmark for the Person.
     * 
     * @param person the person who will get a new bookmark
     * @param content the content that will be bookmarked
     */
    public void addBookmark(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.add(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    }

    /**
     * Deletes a Content bookmark from a Person's bookmarks.
     * 
     * @param person who the bookmark will be deleted from
     * @param content which Content's bookmark will be deleted
     */
    public void deleteBookmark(Person person, Content content) {
        List<Person> bookmarkers = content.getBookmarkers();
        bookmarkers.remove(person);
        content.setBookmarkers(bookmarkers);
        elementRepository.save(content);
    }
}
