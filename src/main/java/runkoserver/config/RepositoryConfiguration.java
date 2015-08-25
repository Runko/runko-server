package runkoserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import runkoserver.controller.AreaController;
import runkoserver.domain.Area;
import runkoserver.domain.Person;
import runkoserver.repository.AreaRepository;
import runkoserver.service.AreaService;
import runkoserver.service.PersonService;

/**
 * Configuration for setting repositories in example for testing.
 * @see runkoserver.repository
 */
@Configuration
public class RepositoryConfiguration {
    
    @Autowired
    PersonService personService;
    
     @Autowired
     AreaService contentAreaService;
    
     private Person p;
     
    @Autowired
    protected void addTestPerson() {
        p = new Person("Matti");
        p.setUsername("testi");
        p.setPassword("testi");
        p.setDescription("Olen kova partiolainen!!! Matti Rocks!!!!!!!!!!!!!!!!");
        p.setUrlToPhoto("http://giveitlove.com/wp-content/uploads/Pirate-Cat-Halloween-Costume.jpg");
        personService.save(p);
        p = new Person("Timo");
        p.setUsername("testi2");
        p.setPassword("testi2");
        p.setUrlToPhoto(null);
        personService.save(p);
    }
    
}
