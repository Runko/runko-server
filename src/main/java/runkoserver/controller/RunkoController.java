package runkoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import runkoserver.service.ContentServices;

@Controller
@RequestMapping("/")
public class RunkoController {
    
    @Autowired
    ContentServices contentServices;
    
    @RequestMapping(method = RequestMethod.GET)
    public String Hello(Model model) {
        model.addAttribute("content", contentServices.findAll());
        return "index";
    }
}
