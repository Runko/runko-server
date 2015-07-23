/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.SimpleContent;
import runkoserver.repository.ContentRepository;
import java.util.List;
import runkoserver.domain.Content;

@Service
public class ContentService implements RepoService{

    @Autowired
    ContentRepository repository;

    public boolean save(SimpleContent simpleContent) {
        String name = simpleContent.getName();
        if (name != null && !name.trim().isEmpty()) {
            repository.save(simpleContent);
            return true;
        }
        return false;
    }

    public List<Content> findAll() {
        return repository.findAll();
    }
    
    public Content findById(Long id) {
        return repository.findOne(id);
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
}
