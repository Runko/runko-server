package runkoserver.contoller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Area;
import runkoserver.domain.Element;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.AreaService;
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
    AreaService areaService;
    
    @Autowired
    PersonService personService;
    
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.GET)
    public String viewElement(Model model,
            @PathVariable Long id,
            Principal principal) {
        Element content = elementService.findById(id);
        model.addAttribute(ATTRIBUTE_CONTENT, content);
        model.addAttribute(ATTRIBUTE_PERSON, personService.findByUsername(principal.getName()));
        
        return FILE_CONTENT;
    }
    
    @RequestMapping(value = LINK_VIEW_ID, method = RequestMethod.DELETE)
    public String deleteElement(@PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        Element element = elementService.findById(id);
        
        if (elementService.deleteElement(id, personService.findByUsername(principal.getName()))) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_DELETE_FAIL);
        }
        
        return REDIRECT + LINK_FRONTPAGE;
    }
    
    @RequestMapping(value = LINK_CONTENTFORM, method = RequestMethod.GET)
    public String createContentForm(Model model) {
        model.addAttribute(ATTRIBUTE_AREA, areaService.findAllAreas());
        return FILE_CONTENTFORM;
    }
    
    @RequestMapping(value = LINK_CONTENTFORM, method = RequestMethod.POST)
    public String createContent(RedirectAttributes redirectAttributes,
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String textArea,
            @RequestParam(required = false) List<Long> areaIds,
            Principal principal) {
        Person person = personService.findByUsername(principal.getName());
        List<Area> areas = areaService.findListedAreasById(areaIds);
        Element content = elementService.createContent(name, textArea, person, areas);
        
        if(elementService.saveElement(content)) {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_CONTENT_SAVE_FAIL);
            return REDIRECT_HOME;
        }
        
        return REDIRECT + LINK_ELEMENT + "/" + content.getId();
    }
}
