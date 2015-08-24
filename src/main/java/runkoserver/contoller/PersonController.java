package runkoserver.contoller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
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
import runkoserver.service.ElementService;
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
    
    @Autowired
    ElementService elementService;

    /**
     * GET-method to view the currently logged in user's profile.
     *
     * @param model Spring model object
     * @param principal Spring object that knows who is logged in
     * @return path to HTML file for profile view
     */
    @RequestMapping(value = LINK_PROFILE, method = RequestMethod.GET)
    public String getProfile(Model model, Principal principal) {
        Person person = personService.findByUsername(principal.getName());

        model.addAttribute(ATTRIBUTE_PERSON, person);
        return FILE_PROFILE;
    }

    /**
     * GET-method to view the profile editing form.
     *
     * @param redirectAttributes Spring object to carry redirect attributes
     * @param model Spring model object
     * @param principal Spring object that knows who is logged in
     * @return path to the HTML file for profile editing form if user is logged
     * in, otherwise redirect URL to the front page
     */
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

    /**
     * POST-method to update the user's profile.
     *
     * @param redirectAttributes Spring object to carry redirect attributes
     * @param principal Spring object that knows who is logged in
     * @param urlToPhoto URL of user photo for profile
     * @param description user's profile description
     * @return redirect URL to the profile page is user is logged in, otherwise
     * redirect URL to the front page
     */
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
    
    @RequestMapping(value = LINK_CONTENT_MANAGER, method = RequestMethod.GET)
    public String getContentManager(RedirectAttributes redirectAttributes,
            Model model,
            Principal principal) {
        Person currentUser = personService.findByUsername(principal.getName());
        model.addAttribute(ATTRIBUTE_CONTENTS, elementService.findByOwner(currentUser));
        model.addAttribute(ATTRIBUTE_PERSON, currentUser);
        
        return FILE_CONTENT_MANAGER;
    }
}
