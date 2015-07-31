package runkoserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import runkoserver.domain.Person;
import runkoserver.service.PersonService;

/**
 * Configuration for setting repositories in example for testing.
 * @see runkoserver.repository
 */
@Configuration
public class RepositoryConfiguration {
    
    @Autowired
    PersonService personRepository;
    
    @Autowired
    protected void addTestPerson() {
        Person p = new Person("Matti");
        p.setUsername("testi");
        p.setPassword("testi");
        personRepository.save(p);
    }
}
