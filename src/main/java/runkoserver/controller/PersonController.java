package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.domain.Person;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Person objects.
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "person";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addPerson(@ModelAttribute Person person) {
        personService.save(person);

        return "redirect:/persons";
    }
}
