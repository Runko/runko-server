package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import runkoserver.domain.Person;
import runkoserver.repository.PersonRepository;

/**
 * Service for all person-repository's handling requests.
 */
@Service
public class PersonService implements RepoService{
    
    @Autowired
    private PersonRepository repository;
    
    public List<Person> findAll() {
        return repository.findAll();
    }
    
    public void save(Person person) {
        if (person.getName() != null && !person.getName().trim().isEmpty()) {
            repository.save(person);
        }
    }
    
    public Person findById(Long Id) {
        return repository.findById(Id);        
    }
    
    public List<Person> findByName(String name) {
        return repository.findByName(name);
    }
    

    public Person findByUsername(String username) {
        return repository.findByUsername(username);
    }
    
    /**
     * Checks if the user on the site has logged in.
     * @return true if the user is logged in, otherwise false
     */
    public boolean userIsLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return !(auth instanceof AnonymousAuthenticationToken);
    }
    
    @Override
    public void delete(Long Id) {
        if (repository.exists(Id)) {
            repository.delete(Id);
        }
    }

    //For tests, unused anywhere else
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    
}
