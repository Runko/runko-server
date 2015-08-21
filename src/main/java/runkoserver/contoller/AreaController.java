package runkoserver.contoller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Areas.
 */
@Controller
@RequestMapping(LINK_AREA_INDEX)
@Transactional
public class AreaController {
    
    @Autowired
    ElementService elementService;
    
    @Autowired
    AreaService areaService;
    
    @Autowired
    PersonService personService;
    
    /**
     * GET-method for rendering a view with the information of a specific Area.
     * 
     * @param model model object for Spring to use
     * @param id the id of the Area whose information will be shown
     * @param principal
     * @return path to the html file that shows Area information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String viewArea(Model model,
            @PathVariable Long id,
            Principal principal) {
        Area area = areaService.findAreaById(id);
        model.addAttribute(ATTRIBUTE_AREA, area);
        model.addAttribute(ATTRIBUTE_IS_SUBSCRIPTED, 
                personService
                .findByUsername(principal.getName())
                                            .isSubscribedToArea(area));
        
        return FILE_AREA;
    }
    
    /**
     * GET-method for rendering the form to create new Areas.
     *
     * @param model model object for Spring to use
     * @param principal tells who is logged in.
     * @return path to the area creation form html file
     */
    @RequestMapping(value = LINK_AREA_FORM, method = RequestMethod.GET)
    public String areaForm(Model model,
            Principal principal) {
        model.addAttribute(ATTRIBUTE_PERSON, personService.findByUsername(principal.getName()));
        return FILE_AREA_FORM;
    }
    
    @RequestMapping(value = LINK_AREA_FORM, method = RequestMethod.POST)
    public String createArea(RedirectAttributes redirectAttributes,
            Principal principal,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) Boolean visibility) {
        Area area = areaService.createArea(name, 
                personService.findByUsername(principal.getName()), visibility);
        if (areaService.save(area)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_SUCCESS);
            return REDIRECT + LINK_AREA_INDEX + "/" + area.getId();
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_FAIL);
        }
        return REDIRECT + LINK_FRONTPAGE;
    }
    
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.POST)
    public String subscribeArea(@PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        Person person = personService.findByUsername(principal.getName());
        Area area = areaService.findAreaById(id);
        
        if (person.isSubscribedToArea(area)) {
            person.removeSubscription(area);
            area.removeSubscriber(person);
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_STOP);
        } else {
            person.addSubscription(area);
            area.addSubscriber(person);
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_START);
        }
        
        return REDIRECT + LINK_AREA_INDEX + LINK_VIEW_ID;
    }
}
