/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import runkoserver.domain.Person;
import runkoserver.repository.PersonRepository;

/**
 *
 * @author Timo
 */

@Controller
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    
    public Person create(@ModelAttribute("person") Person person) {
        Person p = findByKuksaId(person.getKuksaId());    
        if(p!=null) {
            return p;
        }
         return personRepository.save(person);
 
    }
    
    public Person findByKuksaId(String kuksaId) {
        return personRepository.findByKuksaId(kuksaId);
    }
    
}
