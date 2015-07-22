package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.domain.Person;
import runkoserver.repository.PersonRepository;

@Controller
@RequestMapping("/persons")
public class PersonController {
    
    @Autowired
    PersonRepository repository;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("persons", repository.findAll());
        return "person";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String addPerson(@ModelAttribute Person person) {
        String name = person.getName();
        if (name != null && !name.trim().isEmpty()) {
            repository.save(person);
        }
        
        return "redirect:/persons";
    }
}
