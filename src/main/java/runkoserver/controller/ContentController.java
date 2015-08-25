package runkoserver.controller;

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
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import runkoserver.domain.Content;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;

/**
 * Controller class for HTTP requests related to Content-type objects.
 */
@Controller
@RequestMapping(LINK_CONTENT)
@Transactional
public class ContentController {

    @Autowired
    ContentAreaService contentAreaService;

    @Autowired
    PersonService personService;
    

    /**
     * GET-method for rendering a view with the information of a specific
     * Content object.
     *
     * @param redirectAttributes
     * @param id id of the Content whose information will be shown
     * @param model object for Spring to use
     * @param principal To get who is logged in
     * @return path to the html file that shows Content information
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String getContent(RedirectAttributes redirectAttributes,
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Element content = contentAreaService.findElementById(id);

        if (personService.userIsLoggedIn() || content.hasPublicAreas()) {
            model.addAttribute(ATTRIBUTE_CONTENT, content);
                        
            if (principal != null) {
                model.addAttribute(ATTRIBUTE_PERSON, personService.findByUsername(principal.getName()));
                model.addAttribute(ATTRIBUTE_IS_BOOKMARKED, personService.findIfBookmarked(personService.findByUsername(principal.getName()), (Content) contentAreaService.findElementById(id)));
            }
            return FILE_CONTENT;
        }
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_PAGE_NOT_AVAILABLE);
        return REDIRECT_HOME;
    }

    /**
     * /**
     * GET-method for rendering the form to modify existing content.
     *
     * @param id
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.GET)
    public String contentEditForm(@PathVariable Long id, Model model) {
        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAllAreas());
        model.addAttribute(ATTRIBUTE_CONTENT, contentAreaService.findElementById(id));

        return FILE_CONTENT_EDIT;
    }

    /**
     *
     * GET-method for rendering the form to create new content.
     *
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = LINK_CONTENT_FORM, method = RequestMethod.GET)
    public String contentNewForm(Model model) {
        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAllAreas());

        return FILE_CONTENT_FORM;
    }

    /**
     * POST-method to create a new Content.
     *
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redirected to
     * @param name Name of new content
     * @param textArea textfield of content
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
        Element content = contentAreaService.createContent(name, textArea, areaIds, p);

        if (contentAreaService.saveElement(content)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_SUCCESS);
            
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_FAIL);
            
        }
        return REDIRECT+LINK_FRONTPAGE;
    }

    /**
     * POST-method to modify Contents data.
     *
     * @param id identifies which content is modified
     * @param redirectAttributes
     * @param name content's title
     * @param textArea content's text area
     * @param areaIds list of areas where content is connected
     * @param principal who is logged in
     * @return back to index
     */
    @RequestMapping(value = "/edit" + LINK_VIEW_ID, method = RequestMethod.POST)
    public String updateSimpleContent(@PathVariable Long id, RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal
    ) {

        if (contentAreaService.updateContent(id, name, textArea, areaIds, personService.findByUsername(principal.getName()))) {
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
     * @param redirectAttributes a Spring object to carry attributes from this
     * method to the one that the user is next redi
     * @param principal who is logged in
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteElement(@PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        Element element = contentAreaService.findElementById(id);

        if (contentAreaService.deleteElement(element.getId(), personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_SUCCESS + element.getName());
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_FAIL);
        }
        return REDIRECT+LINK_FRONTPAGE;
    }
    
//    /**
//     * GET-method for rendering the form to create new content.
//     * 
//     * @param model object for spring to use
//     * @return path to the content creation form html file 
//     */
//    @RequestMapping(value = LINK_CONTENT_FANCYFORM, method = RequestMethod.GET)
//    public String fancyContentForm(Model model) {
//        model.addAttribute(ATTRIBUTE_AREA, contentAreaService.findAllAreas());
//        
//        return FILE_FANCY_CONTENT_FORM;
//    }
    
    /**
     * 
     *
     * @param id which area is subscripted or unsubscripted
     * @param whereICome tells which URL we should redirect
     * @param principal who is logged
     * @param redirectAttributes message
     * @return url where we go
     */
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.POST)
    public String bookmarkedContent(@PathVariable Long id,
            @RequestParam(required = false) String whereICome,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Person person = personService.findByUsername(principal.getName());
        if (personService.addBookmark(person, (Content) contentAreaService.findElementById(id))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_BOOKMARKED);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_UNBOOKMARKED);
            if (null != whereICome) {
                if (whereICome.equals("bookmarks")) {
                    return REDIRECT + LINK_PERSONS + LINK_BOOKMARK;
                }
                if (whereICome.equals("CM")) {
                    return REDIRECT + LINK_PERSONS + LINK_CONTENT_MANAGER;
                }
            } 
        }

        return REDIRECT + LINK_CONTENT + LINK_VIEW_ID;
    }
    
//    @RequestMapping(value = LINK_CONTENT_FANCYFORM, method = RequestMethod.POST)
//    public String postFancyContent(RedirectAttributes redirectAttributes,
//            @RequestParam(required = true) String name,
//            @RequestParam(required = true) String textElement,
//            @RequestParam(required = false) List<Long> areaIds,
//            Principal principal) {
//
//        Person p = personService.findByUsername(principal.getName());
//        Content fancyContent = contentAreaService.createFancyContent(name, textElement, areaIds, p);
//
//        if (contentAreaService.saveContent(fancyContent)) {
//            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_SUCCESS);
//            
//        } else {
//            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_FAIL);
//            
//        }
//        return REDIRECT+LINK_FRONTPAGE;
//    }

}
