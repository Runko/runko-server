package runkoserver.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.PersonService;

@Controller
@RequestMapping(LINK_LOGIN_LOGOUT)
public class RunkoController {
    
    @Autowired
    PersonService personService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String loginLogout(RedirectAttributes redirectAttributes) {
        if (personService.isUserLoggedIn()) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_LOGIN_DEFAULT);
            return REDIRECT + LINK_SAML_LOGOUT;
        }        
        return REDIRECT + LINK_SAML_LOGIN;
    }
}
