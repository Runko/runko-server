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

@Controller
@RequestMapping("/content")

public class ContentController {

    @Autowired
    ContentService contentService;

    @Autowired
    AreaService areaService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getContent(@PathVariable Long id, Model model) {
        model.addAttribute("content", contentService.findById(id));

        return "/content/simple_content";
    }

    @RequestMapping(value = "/simpleform", method = RequestMethod.GET)
    public String simpleContentForm(Model model) {
        model.addAttribute("area", areaService.findAll());
        return "/content/simple_content_form";
    }

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
