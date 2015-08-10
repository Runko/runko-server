package runkoserver.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Area;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;


/**
 * Controller class for HTTP requests related to Area objects.
 */
@Controller
@RequestMapping(LINK_AREA_INDEX)
public class AreaController {
    
    @Autowired
    ContentAreaService contentAreaService;
    
    @Autowired
    PersonService personService;

    /**
     * GET-method for rendering a view with the information of a specific Area.
     *
     * @param id the id of the Area whose information will be shown
     * @param model model object for Spring to use
     * @return path to the html file that shows Area information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getArea(@PathVariable Long id, Model model) {
        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAreaById(id));
        return FILE_AREA;
    }

    /**
     * GET-method for rendering the form to create new Areas.
     *
     * @param model  model object for Spring to use
     * @param principal tells who is logged in. 
     * @return path to the area creation form html file
     */
    @RequestMapping(value = LINK_AREA_FORM, method = RequestMethod.GET)
    public String areaForm(Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_PERSON, person);
        return FILE_AREA_FORM;
    }
    
    /**
     * POST-method to create a new Area.
     *
     * @param redirectAttributes a Spring object to carry attributes from this method to
     * the one that the user is next redirected to
     * @param ownerId  tells who owns area
     * @param name Area's title
     * @param visibility tells if area is public or not
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_AREA_FORM, method = RequestMethod.POST)
    public String postAreaContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) Long ownerId,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) Boolean visibility){
        
        Area area = contentAreaService.createArea(name, personService.findById(ownerId), visibility);
        
        
        if (contentAreaService.saveArea(area)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_FAIL);
        }
        return REDIRECT_HOME;
    }
}
