package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Area;
import runkoserver.repository.AreaRepository;
import runkoserver.service.AreaService;

@Controller
@RequestMapping("/area")
public class AreaController {

    @Autowired
    AreaService areaService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getArea(@PathVariable Long id, Model model) {
        model.addAttribute("area", areaService.findById(id));
        
        return "/area/arealist";
    }
    
    @RequestMapping(value = "/areaform", method = RequestMethod.GET)
    public String areaForm() {
        return "/area/area_form";
    }

    @RequestMapping(value = "/areaform", method = RequestMethod.POST)
    public String postAreaContent(RedirectAttributes redirectAttributes,
            @ModelAttribute Area area) {
        if (areaService.save(area)) {
            redirectAttributes.addFlashAttribute("message", "Uusi alue tallennettu!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Alueen tallentaminen epäonnistui");
        }
        return "redirect:/";
    }
}
