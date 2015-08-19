package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import runkoserver.domain.Person;
import runkoserver.repository.PersonRepository;

/**
 * Service for PersonRepository's handling requests.
 */

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;
    
    public List<Person> findAll() {
        return personRepository.findAll();
    }
    
    public void save(Person person) {
        if (person.getUsername() != null && !person.getUsername().trim().isEmpty()) {
            personRepository.save(person);
        }
    }
    
    public Person findById(Long id) {
        return personRepository.findById(id);
    }
    
    public Person findByUsername(String username) {
        Person person = personRepository.findByUsername(username);
        
        if (isUserLoggedIn() && person == null) {
            person = new Person(username);
            save(person);
        }
        
        return person;
    }
    
    public boolean isUserLoggedIn() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
    
    public void delete(Long id) {
        if (personRepository.exists(id)) {
            personRepository.delete(id);
        }
    }
    
    public void updatePerson(String username, String urlToPhoto, String description) {
        Person person = personRepository.findByUsername(username);
        person.setUrlToPhoto(urlToPhoto);
        person.setDescription(description);
        personRepository.save(person);
    }
}
