package runkoserver.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.SimpleContent;
import runkoserver.repository.ContentRepository;
import runkoserver.service.AreaService;
import runkoserver.service.ContentService;

/**
 * Controller class for HTTP requests related to Content-type objects.
 */
@Controller
@RequestMapping("/content")

public class ContentController {

    @Autowired
    ContentService contentService;


    @Autowired
    AreaService areaService;

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
     * /**
     * GET-method for rendering the form to create new content.
     * @param model object for spring to use
     * @return path to the content creation form html file
     */
    @RequestMapping(value = "/simpleform", method = RequestMethod.GET)
    public String simpleContentForm(Model model) {
        model.addAttribute("area", areaService.findAll());
        return "/content/simple_content_form";
    }

    /**
     * POST-method to create a new Content.
     * 
     * @param redirectAttributes a Spring object to carry attributes from this method to
     * the one that the user is next redirected to
     * @param name  Name of new content
     * @param textArea  textfield of content
     * @param areaIds  List with ares where content is connected
     * @return the URL path that the user will be redirected to
     */
    @RequestMapping(value = "/simpleform", method = RequestMethod.POST)
    public String postSimpleContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds) {

        SimpleContent simpleContent = new SimpleContent();
        simpleContent.setName(name);
        simpleContent.setTextArea(textArea);

        List<Area> areas = new ArrayList<>();
        
        if (areaIds != null) {
            for (Long areaId : areaIds) {
                Area a = areaService.findById(areaId);
                if (a != null) {
                    areas.add(a);
                }
            }
        }
        
        simpleContent.setAreas(areas);

        if (contentService.save(simpleContent)) {
            redirectAttributes.addFlashAttribute("message", "Uutta sisältöä tallennettu!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Sisällön tallentaminen epäonnistui");
        }
        return "redirect:/";
    }
}
