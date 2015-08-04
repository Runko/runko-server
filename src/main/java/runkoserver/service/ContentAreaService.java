package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
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
    public boolean saveContent(Content content) {
        if (content != null) {
            contentRepository.save(content);
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
    
    public boolean deleteContent(Long id) {
        if (contentRepository.exists(id)) {
            contentRepository.delete(id);
            return true;
        }
        return false;
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
    
    public Area findById(Long id) {
        return areaRepository.findOne(id);
    }
    
    //shared interactions
    
    /**
     * Deletion ONLY for testing purposes
     * @return was repositories emptied.
     */
    public boolean deleteAll() {
        contentRepository.deleteAll();
        areaRepository.deleteAll();
        
        if(findAllAreas().isEmpty() && findAllContent().isEmpty()) {
            return true;
        }
        return false;
    }
}
