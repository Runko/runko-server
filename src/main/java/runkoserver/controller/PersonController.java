package runkoserver.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Person objects.
 */
@Controller
@RequestMapping(LINK_PERSONS)
@Transactional
public class PersonController {

    @Autowired
    PersonService personService;
    
    @Autowired
    ContentAreaService contentAreaService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute(FILE_PERSONS, personService.findAll());
        return FILE_PERSON;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addPerson(@ModelAttribute Person person) {
        personService.save(person);

        return REDIRECT+LINK_PERSONS;
    }

    @RequestMapping(value = LINK_PROFILE, method = RequestMethod.GET)
    public String getProfile(RedirectAttributes redirectAttributes, Model model, Principal principal) {
       Person person =personService.findByUsername(principal.getName());
       model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
       model.addAttribute(ATTRIBUTE_PERSON, person);
      
       return LINK_PROFILE;
    }
    
    @RequestMapping(value = LINK_PROFILE + "/edit" + LINK_VIEW_ID, method = RequestMethod.GET)
    public String getProfileEdit(RedirectAttributes redirectAttributes, Model model, Principal principal) {
       Person person =personService.findByUsername(principal.getName());
       model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
       model.addAttribute(ATTRIBUTE_PERSON, person);
       
       return FILE_PROFILE_EDIT;
    }
}
