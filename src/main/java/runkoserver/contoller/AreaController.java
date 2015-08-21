package runkoserver.contoller;

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
     * @return path to the HTML file that shows Area information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String viewArea(Model model,
            @PathVariable Long id,
            Principal principal) {

        model.addAttribute(ATTRIBUTE_AREA, areaService.findAreaById(id));
        //Subscription

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

    /**
     * POST-method to create a new Area.
     *
     * @param redirectAttributes Spring object to carry attributes to the page
     * the user is redirected to
     * @param principal Spring object that knows who is logged in
     * @param name name of the new Area
     * @param visibility visibility value of the new Area
     * @return
     */
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
}
