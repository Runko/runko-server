package runkoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/")
public class RunkoController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String Hello() {
        return "/WEB-INF/templates/index.html";
    }
}
