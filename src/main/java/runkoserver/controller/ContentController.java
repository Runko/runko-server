package runkoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/content")
public class ContentController {

    @RequestMapping(value = "/simpleform", method = RequestMethod.GET)
    public String simpleContentForm() {
        return "simple_content_form";
    }
}
