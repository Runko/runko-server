package runkoserver.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.domain.Person;
import runkoserver.service.AreaService;
import runkoserver.service.ContentService;
import runkoserver.service.PersonService;

/**
 * The main controller class to render front page etc.
 */
@Controller
@RequestMapping("/")
public class RunkoController {
    
    @Autowired
    ContentService contentService;
    
    @Autowired
    AreaService areaService;
    
    @Autowired
    PersonService personService;
    
    /**
     * Get method for main-page.
     * @param model object for spring to use
     * @return back to front page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String Hello(Model model) {
        model.addAttribute("content", contentService.findAll());
        model.addAttribute("area", areaService.findAll());
        return "index";
    }
    
    /**
     * GET-method for current user's profile-view.
     * @param model object for spring to use
     * @param principal access to user-token
     * @return profile-view
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(Model model, Principal principal) {
        String username = principal.getName();
        Person p = personService.findByUsername(username);
        
        model.addAttribute("person", p);
        
        return "profile";
    }
}
