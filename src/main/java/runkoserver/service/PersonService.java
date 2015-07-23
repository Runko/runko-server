package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Person;
import runkoserver.repository.PersonRepository;

@Service
public class PersonService {
    
    @Autowired
    PersonRepository repository;
    
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
    
    public void delete(Long Id) {
        if (repository.exists(Id)) {
            repository.delete(Id);
        }
    }
}
