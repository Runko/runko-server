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
import runkoserver.service.AreaService;

/**
 * Controller class for HTTP requests related to Area objects.
 */
@Controller
@RequestMapping("/area")
public class AreaController {

    @Autowired
    AreaService areaService;

    /**
     * GET-method for rendering a view with the information of a specific Area.
     *
     * @param id the id of the Area whose information will be shown
     * @param model model object for Spring to use
     * @return path to the html file that shows Area information
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getArea(@PathVariable Long id, Model model) {
        model.addAttribute("area", areaService.findById(id));

        return "/area/arealist";
    }

    /**
     * GET-method for rendering the form to create new Areas.
     *
     * @return path to the area creation form html file
     */
    @RequestMapping(value = "/areaform", method = RequestMethod.GET)
    public String areaForm() {
        return "/area/area_form";
    }

    /**
     * POST-method to create a new Area.
     *
     * @param redirectAttributes a Spring object to carry attributes from this method to
     * the one that the user is next redirected to
     * @param area the Area object that Spring creates based on the parameters
     * that arrive in the POST-request
     * @return the URL path that the user will be redirected to
     */
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
