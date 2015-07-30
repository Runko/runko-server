package runkoserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import runkoserver.domain.Person;
import runkoserver.domain.PersonUserDetails;
import runkoserver.repository.PersonRepository;

/**
 * Adapter to turn PersonRepository to be compatible for AuthenticationManagerBuilder.
 * @see runkoserver.config.SecurityConfiguration
 * @see runkoserver.repository.PersonRepository
 */
@Service
public class PersonUserDetailsService implements UserDetailsService{
    
    @Autowired
    private PersonRepository personRepository;
    
    /**
     * Searches the PersonRepository with given username.
     * @param username username which is set in Person-class
     * @return PersonUserDetails, which is compatible with the service. If not found, return the exception.
     * @throws UsernameNotFoundException If repository does not contain requested username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person p = personRepository.findByUsername(username);
        
        if (p == null) {
            throw new UsernameNotFoundException(username);
        } else {
            PersonUserDetails details = new PersonUserDetails(p);
            return details;
        }
    }
    
}
