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
     * Checks if user is already logged in and if so, redirects the user to the
     * logout page (which should then after automatic logout redirect the user
     * to the login page), otherwise redirects straight to the login page.
     *
     * @return redirect to logout or login URL
     */
    @RequestMapping(value = LINK_LOGIN_LOGOUT, method = RequestMethod.GET)
    public String logout() {
        
        if (personService.userIsLoggedIn()) {;
            return REDIRECT + LINK_LOGOUT;
        }
        
        return REDIRECT + LINK_LOGIN;
    }
}
