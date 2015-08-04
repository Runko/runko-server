package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
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

    public boolean deleteContent(Long id) {
        if (contentRepository.exists(id)) {
            Content content = contentRepository.findOne(id);
            deleteContentFromAreas(content);
            contentRepository.delete(content.getId());            
            return true;
        }
        return false;
    }
    
    public SimpleContent createSimpleContent(String name, String textArea, List<Long> areaIds) {
        SimpleContent content = new SimpleContent();
        content.setName(name);
        content.setTextArea(textArea);
        if (areaIds != null) {
            content.setAreas(findListedAreasById(areaIds));
        } else {
            content.setAreas(new ArrayList<Area>());
        }

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

    private List<Area> findListedAreasById(List<Long> areaIds) {
        List<Area> areas = new ArrayList<Area>();
        for (Long id : areaIds) {
            areas.add(findAreaById(id));
        }
        return areas;
    }
    
    private void saveContentToAreas(Content content) {
        for (Area area : content.getAreas()) {
            area.addContent(content);
            areaRepository.save(area);
        }
    }
    
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
    
}
