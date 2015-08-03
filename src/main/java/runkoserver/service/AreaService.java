package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.repository.AreaRepository;

/**
 *  Service for all area-repository's handling requests.
 */
@Service
public class AreaService implements RepoService {

    @Autowired
    AreaRepository repository;

    public boolean save(Area area) {
        String name = area.getName();
        if (name != null && !name.trim().isEmpty()) {
            repository.save(area);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        if (repository.exists(id)) {
            repository.delete(id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Area> findAll() {
        return repository.findAll();
    }
    
    /**
     * Finds all Areas that have visibility set as true.
     * @return List of public Areas
     */
    public List<Area> findAllPublicAreas() {
        return repository.findByVisibilityTrue();
    }

    public Area findById(Long id) {
        return repository.findOne(id);
    }

    public List<Area> findByIds(List<Long> ids) {
        List<Area> areas = new ArrayList<>();

        for (Long id : ids) {
            Area area = repository.findOne(id);

            if (area != null) {
                areas.add(area);
            }
        }
        
        return areas;
    }
    
    public void addContentToAreas(List<Area> areas, Content content) {
        for (Area area : areas) {
            area.getContents().add(content);
        }
    }
    
    public void deleteContentFromAllAreas(Content content) {
        for (Area area : content.getAreas()) {
            area.getContents().remove(content);
        }
    }
}
