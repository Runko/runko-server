package runkoserver.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.MESSAGE_CONTENT_MODIFY_SUCCESS;
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

        return REDIRECT + LINK_PERSONS;
    }

    /**
     * GET - method to open person's profile including owned contents
     *
     * @param redirectAttributes
     * @param model
     * @param principal
     * @return profile page
     */
    @RequestMapping(value = LINK_PROFILE, method = RequestMethod.GET)
    public String getProfile(RedirectAttributes redirectAttributes, Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
        model.addAttribute(ATTRIBUTE_PERSON, person);

        return FILE_PROFILE;
    }

    /**
     *
     * @param redirectAttributes
     * @param model
     * @param principal
     * @return to profile's edit form
     */
    @RequestMapping(value = LINK_PROFILE + LINK_EDIT + LINK_VIEW_ID, method = RequestMethod.GET)
    public String getProfileEdit(RedirectAttributes redirectAttributes, Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
        model.addAttribute(ATTRIBUTE_PERSON, person);

        return FILE_PROFILE_EDIT;
    }

    /**
     * POST-method to modify person's profile.
     *
     * @param id identifies which content is modified
     * @param redirectAttributes
     * @param urlToPhoto link to person's photo
     * @param description
     * @return back to person's updated profile
     */
    @RequestMapping(value = LINK_PROFILE + LINK_EDIT + LINK_VIEW_ID, method = RequestMethod.POST)
    public String updatePersonProfile(@PathVariable Long id, RedirectAttributes redirectAttributes,
            @RequestParam(required = false) String urlToPhoto,
            @RequestParam(required = false) String description) {
        personService.updatePerson(id, urlToPhoto, description);
       redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_MODIFY_SUCCESS);
       
       
        return REDIRECT+LINK_PERSONS+LINK_PROFILE;
    }
    
    @RequestMapping(value = LINK_CONTENT_MANAGER, method = RequestMethod.GET)
    public String getContentManager(RedirectAttributes redirectAttributes, Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
        model.addAttribute(ATTRIBUTE_PERSON, person);

        return FILE_CONTENT_MANAGER;
    }
    
    @RequestMapping(value = LINK_BOOKMARK, method = RequestMethod.GET)
    public String getBookmarks(RedirectAttributes redirectAttributes, Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_CONTENTS, contentAreaService.findByOwner(person));
        model.addAttribute(ATTRIBUTE_PERSON, person);
        
        return FILE_BOOKMARK;
    }
}
