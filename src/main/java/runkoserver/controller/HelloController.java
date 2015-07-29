package runkoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
@ResponseBody
/**
 * Good memory of where it all began.
 */
public class HelloController {
    
    @RequestMapping(method = RequestMethod.GET)
    public String sayHello() {
        String who = "werld";
        
        return "Hello " + who; 
    }
}
