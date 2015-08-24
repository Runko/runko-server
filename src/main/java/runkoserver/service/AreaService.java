package runkoserver.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.repository.AreaRepository;

/**
 * Service for Area-database-interactions.
 */
@Service
public class AreaService {

    @Autowired
    AreaRepository areaRepository;

    /**
     * Saves an Area to the database.
     *
     * @param area the Area to be saved
     * @return true if Area was successfully saved, false otherwise
     */
    public boolean save(Area area) {
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
     * Creates a new area without saving it to repository.
     *
     * @param name area's unique name
     * @param owner area's owner
     * @param visibility area's visibility, public or private (for
     * authenticated)
     * @return created area
     */
    public Area createArea(String name, Person owner, Boolean visibility) {
        Area area = new Area();
        area.setName(name);
        area.setOwner(owner);
        area.setVisibility(visibility);

        return area;
    }

    public Set<Area> findListedAreasById(List<Long> areaIds) {
        Set<Area> areas = new HashSet<>();

        for (Long id : areaIds) {
            areas.add(findAreaById(id));
        }

        return areas;
    }

    /**
     * Goes through the list of areas that an element has a database connection
     * to, and makes sure the areas also have a connection to the element.
     *
     * @param element the element whose area connections will be added
     */
    public void addElementToAreas(Element element) {
        for (Area area : element.getAreas()) {
            Set<Element> elements = area.getElements();

            if (!elements.contains(element)) {
                elements.add(element);
            }
            area.setElements(elements);
            areaRepository.save(area);
        }
    }

    /**
     * Goes through the list of areas that an element has a database connection
     * to, and removes any connections from the areas to the element -- but not
     * from the element to the areas.
     *
     * @param element the element that area connections will be removed
     */
    public void deleteElementFromAreas(Element element) {
        for (Area area : element.getAreas()) {
            Set<Element> elements = area.getElements();

            if (elements.contains(element)) {
                elements.remove(element);
            }
            area.setElements(elements);
            areaRepository.save(area);
        }
    }
}
