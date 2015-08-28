package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.*;
import runkoserver.repository.AreaRepository;
import runkoserver.repository.ElementRepository;

/**
 * Class for repository-interactions of Areas.
 */
@Service
public class AreaService {

    @Autowired
    ElementRepository elementRepository;

    @Autowired
    AreaRepository areaRepository;

    /**
     * Saves an Area to the database.
     *
     * @param area the Area to be saved
     * @return true if Area was successfully saved, false otherwise
     */
    public boolean saveArea(Area area) {
        if ((area != null) && (findAreaByName(area.getName()) == null)) {
            areaRepository.save(area);
            return true;
        }
        return false;
    }

    public List<Area> findAllAreas() {
        return (List<Area>) areaRepository.findAll();
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
        area.setElements(new ArrayList<>());

        return area;
    }

    /**
     * Updates an Area's attributes if the currently logged in user is the owner
     * of the Area.
     *
     * @param areaId which area will be updated
     * @param name areas new name
     * @param visibility areas new visibility
     * @param whoIsLogged to check who is logged in
     * @return true if update was successful, false if not
     */
    public boolean updateArea(Long areaId, String name, boolean visibility, Person whoIsLogged) {
        Area area = findAreaById(areaId);
        if (whoIsLogged.getId() == area.getOwner().getId()) {
            area.setName(name);
            area.setVisibility(visibility);

            areaRepository.save(area);
            return true;
        }
        return false;
    }

    /**
     * Deletes an Area if the currently logged in user is the owner of the Area.
     *
     * @param areaid which area will be deleted
     * @param whoIsLoggedIn to check who is logged in
     * @return true if deletion was successful, false if not
     */
    public boolean deleteArea(Long areaid, Person whoIsLoggedIn) {

        if (areaRepository.exists(areaid)) {

            Area area = areaRepository.findOne(areaid);
            if (area.getOwner().getId() == whoIsLoggedIn.getId()) {
                if (findAreaById(areaid).getElements().isEmpty()) {
                    areaRepository.delete(areaid);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Finds all areas by id from the given list by their id and returns them as
     * Area-objects. Used by CreateSimpleContent-method
     *
     * @param areaIds list of Area ids
     * @return list of Area-objects
     */
    public List<Area> findListedAreasById(List<Long> areaIds) {
        List<Area> areas = new ArrayList<>();
        for (Long id : areaIds) {
            areas.add(findAreaById(id));
        }
        return areas;
    }

    /**
     * Goes through the list of areas that an element has a database connection
     * to, and makes sure the areas also have a connection to the element.
     *
     * @param element the element that connections will be checked on
     */
    public void saveContentToAreas(Element element) {
        for (Area area : element.getAreas()) {
            area.addElement(element);
            areaRepository.save(area);
        }
    }

    /**
     * Goes through the list of areas that an element has a database connection
     * to, and removes any connections from the areas to the element -- but not
     * from the element to the areas.
     *
     * @param element on which connections will be removed
     */
    public void deleteElementFromAreas(Element element) {
        for (Area area : element.getAreas()) {
            area.deleteElement(element);
            areaRepository.save(area);
        }
    }

    /**
     * Deletes all Areas from the repository.
     *
     * @return true if Area repository is now empty, false if not
     */
    public boolean deleteAllAreas() {
        areaRepository.deleteAll();
        return findAllAreas().isEmpty();
    }

    /**
     * Returns a list of the Areas that a Person owns.
     *
     * @param person whose areas will be found
     * @return list of Areas
     */
    public List<Area> findAreasByOwner(Person person) {
        return areaRepository.findByOwner(person);
    }

    /**
     * Adds a new Area subscriptions for a Person.
     *
     * @param person person to whom subscription will be added
     * @param area which area will be subscribed
     */
    public void addSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.add(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
    }

    /**
     * Deletes an Area subscription from a Person.
     *
     * @param person person whose subscription will be deleted
     * @param area which area will be un-subscribed
     */
    public void deleteSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.remove(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
    }

    /**
     * Goes through all the Areas a user has subscribed to and creates a list of
     * contents published in those areas, ordered by their modification times.
     *
     * @param person the person whose subscriptions will be used
     * @return list of the content in chronological order
     */
    public List<Content> createListOfSubscribedContents(Person person) {
        List<Content> newList = new ArrayList<>();

        // Builds new list without duplicates
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

    public boolean isPublic(Area area) {
        return area.getVisibility();
    }

}
