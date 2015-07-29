package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.SimpleContent;
import runkoserver.service.ContentService;

/**
 * Controller class for HTTP requests related to Content-type objects.
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    ContentService contentService;

    /**
     * GET-method for rendering a view with the information of a specific Content object.
     * 
     * @param id id of the Content whose information will be shown
     * @param model object for Spring to use
     * @return path to the html file that shows Content information
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getContent(@PathVariable Long id, Model model) {
        model.addAttribute("content", contentService.findById(id));
        
        return "/content/simple_content";
    }
    
    /**
     * GET-method for rendering the form to create new content.
     * 
     * @return path to the content creation form html file
     */
    @RequestMapping(value = "/simpleform", method = RequestMethod.GET)
    public String simpleContentForm() {
        return "/content/simple_content_form";
    }

    /**
     * POST-method to create a new Content.
     * 
     * @param redirectAttributes a Spring object to carry attributes from this method to
     * the one that the user is next redirected to
     * @param simpleContent the Content object that Spring creates based on the parameters
     * that arrive in the POST-request
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = "/simpleform", method = RequestMethod.POST)
    public String postSimpleContent(RedirectAttributes redirectAttributes,
            @ModelAttribute SimpleContent simpleContent) {
        if (contentService.save(simpleContent)) {
            redirectAttributes.addFlashAttribute("message", "Uutta sisältöä tallennettu!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Sisällön tallentaminen epäonnistui");
        }
        return "redirect:/";
    }
}
