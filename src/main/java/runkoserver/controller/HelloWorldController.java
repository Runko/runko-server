package runkoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class HelloWorldController {
    
    @RequestMapping("/hello")
    public String Hello() {
        return "Hello World!";
    }
}
