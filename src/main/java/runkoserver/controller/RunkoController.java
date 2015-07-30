package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import runkoserver.service.AreaService;
import runkoserver.service.ContentService;

/**
 * The main controller class to render front page etc.
 */
@Controller
@RequestMapping("/")
public class RunkoController {
    
    @Autowired
    ContentService contentService;
    
    @Autowired
    AreaService areaService;
    /**
     * 
     * @param model object for spring to use
     * @return back to front page
     */
    @RequestMapping(method = RequestMethod.GET)
    public String Hello(Model model) {
        model.addAttribute("content", contentService.findAll());
        model.addAttribute("area", areaService.findAll());
        return "index";
    }
}
