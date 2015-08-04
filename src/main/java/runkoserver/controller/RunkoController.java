package runkoserver.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;

/**
 * The main controller class to render front page etc.
 */
@Controller
@RequestMapping(LINK_HOME)
public class RunkoController {

    @Autowired
    ContentAreaService contentAreaService;

    @Autowired
    PersonService personService;

    /**
     * Get method for main-page.
     *
     * @param model object for spring to use
     * @return back to front page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String Hello(Model model) {
        if (personService.userIsLoggedIn()) {
            model.addAttribute(ATTRIBUTE_AREAS, contentAreaService.findAllAreas());
        } else {
            model.addAttribute(ATTRIBUTE_AREAS, contentAreaService.findAllPublicAreas());
        }

        return FILE_HOME;
    }

    /**
     * GET-method for current user's profile-view.
     *
     * @param model object for spring to use
     * @param principal access to user-token
     * @return profile-view
     */
    @RequestMapping(value = LINK_PROFILE, method = RequestMethod.GET)
    public String showProfile(Model model, Principal principal
    ) {
        String username = principal.getName();
        Person p = personService.findByUsername(username);

        model.addAttribute(ATTRIBUTE_PERSON, p);

        return FILE_PROFILE;
    }
}
