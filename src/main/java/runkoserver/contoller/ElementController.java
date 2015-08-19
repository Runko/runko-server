package runkoserver.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static runkoserver.libraries.Links.*;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

/**
 * Controller listening element-related http-requests.
 */
@Controller
@RequestMapping(LINK_ELEMENT)
@Transactional
public class ElementController {
    
    @Autowired
    ElementService elementService;
    
    @Autowired
    PersonService personService;
    
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String viewElement() {
        
        return REDIRECT_HOME;
    }
    
    @RequestMapping(value = LINK_CONTENTFORM, method = RequestMethod.GET)
    public String createContentForm(Model model) {
        
        return FILE_CONTENTFORM;
    }
}
