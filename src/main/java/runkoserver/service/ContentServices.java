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
public class ContentServices {

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
}
