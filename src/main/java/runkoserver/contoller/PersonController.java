package runkoserver.contoller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.PersonService;

/**
 * Controller for Person related HTTP-requests
 */
@Controller
@RequestMapping
@Transactional
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping(value = LINK_PROFILE, method = RequestMethod.GET)
    public String getProfile(Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());

        model.addAttribute(ATTRIBUTE_PERSON, person);
        return FILE_PROFILE;
    }

    @RequestMapping(value = LINK_PROFILE + LINK_EDIT, method = RequestMethod.GET)
    public String getProfileEditForm(RedirectAttributes redirectAttributes,
            Model model, Principal principal) {
        if (personService.isUserLoggedIn()) {
            Person person = personService.findByUsername(principal.getName());
            model.addAttribute(ATTRIBUTE_PERSON, person);

            return FILE_PROFILE_EDIT;
        }
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_NOT_LOGGEDIN);
        return REDIRECT_HOME;
    }

    @RequestMapping(value = LINK_PROFILE + LINK_EDIT, method = RequestMethod.POST)
    public String updateProfile(RedirectAttributes redirectAttributes,
            Principal principal,
            @RequestParam(required = false) String urlToPhoto,
            @RequestParam(required = false) String description) {
        if (personService.isUserLoggedIn()) {
            personService.updatePerson(principal.getName(), urlToPhoto, description);
            redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_PERSON_MODIFY_SUCCESS);
            
            return REDIRECT + LINK_PROFILE;
        }
        redirectAttributes.addFlashAttribute(ATTRIBUTE_MESSAGES, MESSAGE_NOT_LOGGEDIN);
        return REDIRECT_HOME;
    }
}
