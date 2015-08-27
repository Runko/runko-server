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
@RequestMapping(LINK_AREA)
public class AreaController {

    @Autowired
    AreaService areaService;

    @Autowired
    PersonService personService;

    /**
     * GET-method for rendering a view with the information of a specific Area.
     *
     * @param redirectAttributes Spring object for redirect attributes
     * @param id the id of the Area whose information will be shown
     * @param model model object for Spring to use
     * @param principal Spring object that knows who is logged in
     * @return path to the html file that shows Area information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getArea(RedirectAttributes redirectAttributes, @PathVariable Long id, Model model, Principal principal) {
        Area area = areaService.findAreaById(id);
        area.cleanElementList();
        
        if (personService.userIsLoggedIn() || areaService.isPublic(area)) {
            model.addAttribute(ATTRIBUTE_AREA, area);
            
            if (personService.userIsLoggedIn()) {
                model.addAttribute(ATTRIBUTE_IS_SUBSCRIPTED, personService.findIfSubscripted(personService.findByUsername(principal.getName()), area));
            }
            
            return FILE_AREA;
        }
        
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_PAGE_NOT_AVAILABLE);
        return REDIRECT_HOME;
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
    public String postArea(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) Long ownerId,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) Boolean visibility) {

        Area area = areaService.createArea(name, personService.findById(ownerId), visibility);

        if (areaService.saveArea(area)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SAVE_FAIL);
        }
        
        return REDIRECT + LINK_FRONTPAGE;
    }

    /**
     * GET-method for rendering the form to modify an existing Area.
     *
     * @param id ID of the Area that will be edited
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.GET)
    public String areaEditForm(@PathVariable Long id, Model model) {
        model.addAttribute(ATTRIBUTE_AREA, areaService.findAreaById(id));

        return FILE_AREA_EDIT;
    }

    /**
     * POST-method to update an existing Area.
     * 
     * @param id the id of the Area to be modified
     * @param redirectAttributes a Spring object to carry redirect attributes
     * @param name Area's name
     * @param visibility Area's visibility
     * @param principal Spring object that knows who is logged in
     * @return redirect URL to front page
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.POST)
    public String updateArea(@PathVariable Long id, RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) boolean visibility,
            Principal principal) {

        if (areaService.updateArea(id, name, visibility, personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_MODIFY_SUCCESS);
            return REDIRECT + LINK_AREA + LINK_VIEW_ID;
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_MODIFY_FAIL);
            return REDIRECT + LINK_FRONTPAGE;
        }
    }

    /**
     * Deletes an Area if the currently logged in user is the Area's owner.
     * 
     * @param id the id of the Area to be deleted
     * @param redirectAttributes a Spring object to carry redirect attributes
     * @param principal Spring object that knows who is logged in
     * @return redirect URL to front page
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteArea(@PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        Area area = areaService.findAreaById(id);

        if (areaService.deleteArea(area.getId(), personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_DELETE_SUCCESS + area.getName());
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_DELETE_FAIL);
        }
        return REDIRECT + LINK_FRONTPAGE;
    }

    /**
     * POST-method to subscribe the currently logged in user to an Area.
     * 
     * @param id which area is subscripted or unsubscripted
     * @param whereICome tells which URL we should redirect
     * @param principal Spring object that knows who is logged in
     * @param redirectAttributes Spring object for redirect attributes
     * @return redirect URL to either content manager or Area page 
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.POST)
    public String subscribeArea(@PathVariable Long id,
            @RequestParam(required = false) String whereICome,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Person person = personService.findByUsername(principal.getName());
        if (personService.addSubscribtion(person, areaService.findAreaById(id))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_START);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_AREA_SUBSCRIPTION_STOP);
            if (null != whereICome) {
                if (whereICome.equals("CM")) {
                    return REDIRECT + LINK_PERSONS + LINK_CONTENT_MANAGER;
                }
            }
        }
        return REDIRECT + LINK_AREA + LINK_VIEW_ID;
    }
}
