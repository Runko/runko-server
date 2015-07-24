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
import runkoserver.repository.ContentRepository;
import runkoserver.service.ContentService;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    ContentService contentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getContent(@PathVariable Long id, Model model) {
        model.addAttribute("content", contentService.findById(id));
        
        return "/content/simple_content";
    }
    
    @RequestMapping(value = "/simpleform", method = RequestMethod.GET)
    public String simpleContentForm() {
        return "/content/simple_content_form";
    }

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
