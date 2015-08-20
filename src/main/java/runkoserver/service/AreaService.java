package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.repository.AreaRepository;

/**
 * Service for area-database-interactions.
 */
@Service
public class AreaService {
    
    @Autowired
    AreaRepository areaRepository;
    
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
     * @param visibility area's visibility, public or private(for authenticated)
     * @return created area
     */
    public Area createArea(String name, Person owner, Boolean visibility) {
        Area area = new Area();
        area.setName(name);
        area.setOwner(owner);
        area.setVisibility(visibility);
        
        return area;
    }
    
    public List<Area> findListedAreasById(List<Long> areaIds) {
        List<Area> areas = new ArrayList<>();
        for (Long id : areaIds) {
            areas.add(findAreaById(id));
        }
        return areas;
    }
    
    public void addElementToAreas(Element element) {
        for (Area area : element.getAreas()) {
            List<Element> elements = area.getElements();
            if (!elements.contains(element)) {                
                elements.add(element);
                area.setElements(elements);
                areaRepository.save(area);
            }
        }
    }
    
    public void deleteElementFromAreas(Element element) {
        for (Area area : element.getAreas()) {
            List<Element> elements = area.getElements();
            if (elements.contains(element)) {
                elements.remove(element);
                area.setElements(elements);
                areaRepository.save(area);
            }
        }
    }
}
