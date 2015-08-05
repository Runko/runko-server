package runkoserver.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Content;
import runkoserver.domain.Person;
import runkoserver.domain.SimpleContent;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Content-type objects.
 */
@Controller
@RequestMapping(LINK_CONTENT_INDEX)

public class ContentController {

    @Autowired
    ContentAreaService contentAreaService;
    
    @Autowired
    PersonService personService;

    /**
     * GET-method for rendering a view with the information of a specific
     * Content object.
     *
     * @param id id of the Content whose information will be shown
     * @param model object for Spring to use
     * @return path to the html file that shows Content information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getContent(@PathVariable Long id, Model model) {
        model.addAttribute(ATTRIBUTE_CONTENT, contentAreaService.findContentById(id));

        return FILE_SIMPLECONTENT;
    }

    /**
     * /**
     * GET-method for rendering the form to create new content.
     *
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = LINK_CONTENT_SIMPLEFORM, method = RequestMethod.GET)
    public String simpleContentForm(Model model) {
        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAllAreas());
        return FILE_SIMPLECONTENT_FORM;
    }

    /**
     * POST-method to create a new Content.
     *
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redirected to
     * @param name Name of new content
     * @param textArea textfield of content
     * @param areaIds List with ares where content is connected
     * @param principal To get Who is logged in.
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_CONTENT_SIMPLEFORM, method = RequestMethod.POST)
    public String postSimpleContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal) {

        
        
        Person p = personService.findByUsername(principal.getName());
        SimpleContent simpleContent = contentAreaService.createSimpleContent(name, textArea, areaIds, p);


        if (contentAreaService.saveContent(simpleContent)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE, MESSAGE_CONTENT_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE, MESSAGE_CONTENT_SAVE_FAIL);
        }

        return REDIRECT_HOME;
    }

    /**
     * Deletes a Content and any possible references to it.
     *
     * @param id the id of the Content to be deleted
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redirected to
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteContent(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Content content = contentAreaService.findContentById(id);
        if (contentAreaService.deleteContent(content.getId())) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE, MESSAGE_CONTENT_DELETE_SUCCESS + content.getName());

            return REDIRECT_HOME;
        }
        
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGE, MESSAGE_CONTENT_DELETE_FAIL);
        return REDIRECT_HOME;
    }
}
