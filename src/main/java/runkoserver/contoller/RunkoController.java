package runkoserver.contoller;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

@Controller
public class RunkoController {
    
    @Autowired
    PersonService personService;
    
    @Autowired
    ElementService elementService;
    
    @RequestMapping(value = LINK_HOME, method = RequestMethod.GET)
    public String getHomePage(Model model,
            Principal principal) {
        if (personService.isUserLoggedIn()) {
            Person user = personService.findByUsername(principal.getName());
            List<Element> subscribedElements = elementService.ListFromSubscribedAreas(user);
            model.addAttribute(ATTRIBUTE_CONTENTS, subscribedElements);
            
            return FILE_FRONTPAGE;
        }
        
        return FILE_HOME;
    }
    
    @RequestMapping(value = LINK_LOGIN_LOGOUT, method = RequestMethod.GET)
    public String loginLogout(RedirectAttributes redirectAttributes) {
        if (personService.isUserLoggedIn()) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_LOGIN_DEFAULT);
            return REDIRECT + LINK_SAML_LOGOUT;
        }        
        return REDIRECT + LINK_SAML_LOGIN;
    }
}
