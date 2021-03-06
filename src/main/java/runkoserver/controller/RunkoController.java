package runkoserver.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.domain.Content;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import runkoserver.service.AreaService;
import runkoserver.service.PersonService;

/**
 * The main controller class to render front page etc.
 */
@Controller
@RequestMapping(LINK_HOME)
public class RunkoController {

    @Autowired
    AreaService areaService;

    @Autowired
    PersonService personService;

    /**
     * GET-method for main-page view.
     *
     * @param model Spring model object
     * @return path to front page view html file
     */
    @RequestMapping(method = RequestMethod.GET)
    public String Hello(Model model) {
        if (personService.userIsLoggedIn()) {
            model.addAttribute(ATTRIBUTE_AREAS, areaService.findAllAreas());
        } else {
            model.addAttribute(ATTRIBUTE_AREAS, areaService.findAllPublicAreas());
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
        
        if (personService.userIsLoggedIn()) {
            return REDIRECT + LINK_LOGOUT;
        }
        
        return REDIRECT + LINK_LOGIN;
    }
    
    /**
     * GET-method for displaying the front page.
     * 
     * @param model Spring model object
     * @param principal Spring object that knows who is logged in
     * @return redirect URL to old home page  
     */
    @RequestMapping(value = LINK_FRONTPAGE, method = RequestMethod.GET)
    public String frontPage(Model model, Principal principal) {
        if(!personService.userIsLoggedIn()) {
            return REDIRECT + LINK_HOME;
        }
        
        Person person = personService.findByUsername(principal.getName());
        List <Content> contents = areaService.createListOfSubscribedContents(person);
        model.addAttribute(ATTRIBUTE_CONTENTS, contents);
        
        return FILE_FRONTPAGE;
    }
}
