package runkoserver.contoller;

import java.security.Principal;
import java.util.List;
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
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

/**
 * Controller for Element-related HTTP-requests.
 */
@Controller
@RequestMapping(LINK_ELEMENT)
@Transactional
public class ElementController {

    @Autowired
    ElementService elementService;

    @Autowired
    AreaService areaService;

    @Autowired
    PersonService personService;

    /**
     * GET-method to view the information of a single Element.
     * 
     * @param model Spring model object
     * @param id id of the Element that is to be viewed
     * @param principal Spring object that knows who is logged in
     * @return path to the HTML file that will be used for the view
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String viewElement(Model model,
            @PathVariable Long id,
            Principal principal) {
        
        Element content = elementService.findById(id);
        model.addAttribute(ATTRIBUTE_CONTENT, content);
        model.addAttribute(ATTRIBUTE_PERSON, personService.findByUsername(principal.getName()));

        return FILE_CONTENT;
    }

    /**
     * DELETE-method to delete a single Element.
     * 
     * @param id if of the Element to be deleted
     * @param redirectAttributes Spring object to carry redirect attributes
     * @param principal Spring object that knows who is logged in
     * @return redirect URL
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteElement(@PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        
        Element element = elementService.findById(id);

        if (elementService.deleteElement(id, personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_FAIL);
        }

        return REDIRECT + LINK_FRONTPAGE;
    }

    /**
     * GET-method for the Content creation form view.
     * 
     * @param model Spring model object
     * @return path to HTML file with content creation form
     */
    @RequestMapping(value = LINK_CONTENTFORM, method = RequestMethod.GET)
    public String createContentForm(Model model) {
        model.addAttribute(ATTRIBUTE_AREA, areaService.findAllAreas());
        return FILE_CONTENTFORM;
    }

    /**
     * POST-method to create a new Content.
     * 
     * @param redirectAttributes Spring object to carry redirect attributes
     * @param name name of the Content to be created
     * @param textArea the text that the Content will contain
     * @param areaIds the IDs of the areas the Content will be published in
     * @param principal Spring object that knows who is logged in
     * @return redirect URL to view the new Content
     */
    @RequestMapping(value = LINK_CONTENTFORM, method = RequestMethod.POST)
    public String createContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal) {
        
        Person person = personService.findByUsername(principal.getName());
        List<Area> areas = areaService.findListedAreasById(areaIds);
        Element content = elementService.createContent(name, textArea, person, areas);

        if (elementService.saveElement(content)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_FAIL);
            return REDIRECT_HOME;
        }

        return REDIRECT + LINK_ELEMENT + "/" + content.getId();
    }
}
