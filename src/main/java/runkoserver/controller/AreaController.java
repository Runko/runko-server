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
import runkoserver.service.AreaService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Area objects.
 */
@Controller
@RequestMapping(LINK_AREA_INDEX)
public class AreaController {

    @Autowired
    AreaService contentAreaService;

    @Autowired
    PersonService personService;

    /**
     * GET-method for rendering a view with the information of a specific Area.
     *
     * @param id the id of the Area whose information will be shown
     * @param model model object for Spring to use
     * @param principal
     * @return path to the html file that shows Area information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getArea(@PathVariable Long id, Model model, Principal principal) {
        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAreaById(id));
        model.addAttribute(ATTRIBUTE_IS_SUBSCRIPTED, personService.findIfSubscripted(personService.findByUsername(principal.getName()), contentAreaService.findAreaById(id)));
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
    public String areaForm(Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_PERSON, person);
        return FILE_AREA_FORM;
    }

    /**
     * POST-method to create a new Area.
     *
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redirected to
     * @param ownerId tells who owns area
     * @param name Area's title
     * @param visibility tells if area is public or not
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_AREA_FORM, method = RequestMethod.POST)
    public String postAreaContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) Long ownerId,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) Boolean visibility) {

        Area area = contentAreaService.createArea(name, personService.findById(ownerId), visibility);

        if (contentAreaService.saveArea(area)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_FAIL);
        }
        return REDIRECT + LINK_FRONTPAGE;
    }

    /**
     *
     * @param id which area is subscripted or unsubscripted
     * @param whereICome tells which URL we should redirect
     * @param principal who is logged
     * @param redirectAttributes message
     * @return url where we go
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.POST)
    public String subscriptArea(@PathVariable Long id,
            @RequestParam(required = false) String whereICome,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Person person = personService.findByUsername(principal.getName());
        if (personService.addSubscribtion(person, contentAreaService.findAreaById(id))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_START);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_STOP);
            if (null != whereICome) {
                if (whereICome.equals("CM")) {
                    return REDIRECT + LINK_PERSONS + LINK_CONTENT_MANAGER;
                }
            }
        }

        return REDIRECT + LINK_AREA_INDEX + LINK_VIEW_ID;
    } 
}
