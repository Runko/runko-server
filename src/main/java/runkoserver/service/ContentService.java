/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.SimpleContent;
import runkoserver.repository.ContentRepository;
import java.util.List;
import runkoserver.domain.Area;
import runkoserver.domain.Content;

/**
* Service for all content-repository's handling requests.
 */
@Service
public class ContentService implements RepoService {

    @Autowired
    ContentRepository contentRepository;
    @Autowired
    AreaService areaService;

    public boolean save(SimpleContent simpleContent) {
        String name = simpleContent.getName();
        if (name != null && !name.trim().isEmpty()) {
            contentRepository.save(simpleContent);
            return true;
        }
        return false;
    }

    public void addAreasToContent(List<Area> areas, SimpleContent simpleContent) {
        if (areas != null) {
            areaService.addContentToAreas(areas, simpleContent);
            simpleContent.setAreas(areas);
        } else {
            simpleContent.setAreas(new ArrayList<>());
        }
    }

    public List<Content> findAll() {
        return contentRepository.findAll();
    }

    public Content findById(Long id) {
        return contentRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        if (contentRepository.exists(id)) {
            contentRepository.delete(id);
        }
    }

    @Override
    public void deleteAll() {
        contentRepository.deleteAll();
    }
}
