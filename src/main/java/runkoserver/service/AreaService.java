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
public class AreaService {

    @Autowired
    ElementRepository elementRepository;

    @Autowired
    AreaRepository areaRepository;

    public boolean saveArea(Area area) {
        if ((area != null) && (findAreaByName(area.getName()) == null)) {
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
     *
     * @param areaId which area will be updated
     * @param name areas new name
     * @param visibility areas new visibility
     * @param whoIsLogged to check who is logged in
     * @return
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
     *
     * @param areaid which area will be deleted
     * @param whoIsLoggedIn to check who is logged in
     * @return
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
     * @param areaIds Areas' ids
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
     * Adds connection between given content and it's Areas. Used by delete
     * content.
     *
     * @param element on which the connections are wanted
     */
    public void saveContentToAreas(Element element) {
        for (Area area : element.getAreas()) {
            area.addElements(element);
            areaRepository.save(area);
        }
    }

    /**
     * Deletes connection between given connection and it's Areas-
     *
     * @param element on which connections are disabled 
     */
    public void deleteElementFromAreas(Element element) {
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
    public boolean deleteAllAreas() {
        areaRepository.deleteAll();
        if (findAllAreas().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param person whose areas will be found
     * @return
     */
    public List<Area> findAreasByOwner(Person person) {
        return areaRepository.findByOwner(person);
    }

    /**
     *
     * @param person tells to whom subcription will be added
     * @param area which area wil be added to subcription
     */
    public void addSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.add(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
    }

    /**
     *
     * @param person tells whose subcription will be deleted
     * @param area which area wil be deleted from subcription
     */
    public void deleteSubcriptions(Person person, Area area) {
        List<Person> subscribers = area.getSubscribers();
        subscribers.remove(person);
        area.setSubscribers(subscribers);
        areaRepository.save(area);
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

    public boolean isPublic(Area area) {
        return area.getVisibility();
    }

}
