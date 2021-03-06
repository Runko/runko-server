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
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.domain.Content;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Content objects.
 */
@Controller
@RequestMapping(LINK_CONTENT)
public class ContentController {

    @Autowired
    AreaService areaService;
    
    @Autowired
    ElementService elementService;

    @Autowired
    PersonService personService;
    

    /**
     * GET-method for a view with the information of a specific
     * Content object.
     *
     * @param redirectAttributes Spring object for redirect attributes
     * @param id id of the Content whose information will be shown
     * @param model Spring model object
     * @param principal To get who is logged in
     * @return path to the html file that shows Content information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getContent(RedirectAttributes redirectAttributes,
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Element content = elementService.findElementById(id);

        if (personService.userIsLoggedIn() || content.hasPublicAreas()) {
            model.addAttribute(ATTRIBUTE_CONTENT, content);
                        
            if (principal != null) {
                model.addAttribute(ATTRIBUTE_PERSON, personService.findByUsername(principal.getName()));
                model.addAttribute(ATTRIBUTE_IS_BOOKMARKED, personService.findIfBookmarked(personService.findByUsername(principal.getName()), (Content) elementService.findElementById(id)));
            }
            return FILE_CONTENT;
        }
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_PAGE_NOT_AVAILABLE);
        return REDIRECT_HOME;
    }

    /**
     * GET-method for rendering the form to modify existing content.
     *
     * @param id id of the content to be edited
     * @param model object for spring to use
     * @param principal Spring object that knows who is logged in
     * @param redirectAttributes Spring object to carry redirect attributes
     * @return path to the content creation form html file if the user is the
     * owner of the content, otherwise redirect to front page
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.GET)
    public String contentEditForm(@PathVariable Long id,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        
        Content content = (Content) elementService.findElementById(id);

        if (content.getOwner().getId() == personService.findByUsername(principal.getName()).getId()) {
            model.addAttribute(ATTRIBUTE_AREA, areaService.findAllAreas());
            model.addAttribute(ATTRIBUTE_CONTENT, content);

            return FILE_CONTENT_EDIT;
        }

        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_MODIFY_FAIL);

        return REDIRECT + LINK_FRONTPAGE;
    }

    /**
     * GET-method for  the form view to create a new Content.
     *
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = LINK_CONTENT_FORM, method = RequestMethod.GET)
    public String contentNewForm(Model model) {
        model.addAttribute(ATTRIBUTE_AREA, areaService.findAllAreas());

        return FILE_CONTENT_FORM;
    }

    /**
     * POST-method to create a new Content.
     *
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redirected to
     * @param name Name of new content
     * @param textArea text field of content
     * @param areaIds List with ares where content is connected
     * @param principal To get who is logged in.
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_CONTENT_FORM, method = RequestMethod.POST)
    public String postContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal) {

        Person p = personService.findByUsername(principal.getName());
        Element content = elementService.createContent(name, textArea, areaIds, p);

        if (elementService.saveElement(content)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_SUCCESS);
            
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_FAIL);
            
        }
        return REDIRECT+LINK_FRONTPAGE;
    }

    /**
     * POST-method to modify a Content.
     *
     * @param id identifies which content is modified
     * @param redirectAttributes Spring object for redirect attributes
     * @param name title of the content
     * @param textArea text area of the content
     * @param areaIds list of areas where content is connected
     * @param principal who is logged in
     * @return back to index
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.POST)
    public String updateContent(@PathVariable Long id, RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal) {

        if (elementService.updateContent(id, name, textArea, areaIds, personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_MODIFY_SUCCESS);
            return REDIRECT + LINK_CONTENT + LINK_VIEW_ID;
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_MODIFY_FAIL);
            return REDIRECT+LINK_FRONTPAGE;
        }
    }

    /**
     * Deletes an Element and any possible references to it.
     *
     * @param id the id of the Element to be deleted
     * @param redirectAttributes a Spring object to carry redirect attributes
     * @param principal who is logged in
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteElement(@PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        Element element = elementService.findElementById(id);

        if (elementService.deleteElement(element.getId(), personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_SUCCESS + element.getName());
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_FAIL);
        }
        return REDIRECT+LINK_FRONTPAGE;
    }
    
    /**
     * POST-method to add a new bookmark for the currently logged in user.
     *
     * @param id which area is subscripted or unsubscripted
     * @param whereICome tells which URL we should redirect
     * @param principal Spring object that knows who is logged in
     * @param redirectAttributes Spring object to carry redirect attributes
     * @return redirect URL to the page the user came from
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.POST)
    public String bookmarkedContent(@PathVariable Long id,
            @RequestParam(required = false) String whereICome,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Person person = personService.findByUsername(principal.getName());
        if (personService.addBookmark(person, (Content) elementService.findElementById(id))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_BOOKMARKED);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_UNBOOKMARKED);
            if (null != whereICome) {
                if (whereICome.equals("bookmarks")) {
                    return REDIRECT + LINK_PERSONS + LINK_BOOKMARK;
                }   
            } 
        }

        return REDIRECT + LINK_CONTENT + LINK_VIEW_ID;
    }
}
