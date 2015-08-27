package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.domain.Person;
import runkoserver.domain.Content;
import runkoserver.repository.PersonRepository;

/**
 * Service for all person-repository's handling requests.
 */
@Service
public class PersonService implements RepoService{
    
    @Autowired
    private PersonRepository repository;
    
    @Autowired
    private AreaService areaService;
    
    @Autowired
    private ElementService elementService;
    
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
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        repository.deleteAll();
    }

    public void updatePerson(Long id, String urlToPhoto, String description) {
        Person p = repository.findById(id);
        p.setUrlToPhoto(urlToPhoto);
        p.setDescription(description);
        repository.save(p);
    }

    public boolean findIfSubscripted(Person person, Area area) {
        for (Area sub : person.getSubscriptions()) {
            if(sub.getId() == area.getId()){
            return true;
            }
        }      
       return false;
    }

    public boolean addSubscribtion(Person person, Area area) {
        List<Area> subcriptions=person.getSubscriptions();
        if(findIfSubscripted(person,area)){
            subcriptions.remove(area);
            person.setSubscriptions(subcriptions);
            areaService.deleteSubcriptions(person, area);
            repository.save(person);
            return false;
        }
      
        subcriptions.add(area);
        person.setSubscriptions(subcriptions);
        areaService.addSubcriptions(person, area);
        repository.save(person);
        return true;
    }
    
    public boolean findIfBookmarked(Person person, Content content) {
        for (Content book : person.getBookmarks()) {
            if(book.getId() == content.getId()){
                return true;
            }
        }
        return false;
    }
    
    public boolean addBookmark(Person person, Content content) {
        List<Content> bookmarks = person.getBookmarks();
        if(findIfBookmarked(person, content)){
            bookmarks.remove(content);
            person.setBookmarks(bookmarks);
            elementService.deleteBookmarks(person, content);
            repository.save(person);
            return false;
        }
        
        bookmarks.add(content);
        person.setBookmarks(bookmarks);
        elementService.addBookmarks(person, content);
        repository.save(person);
        return true;
    }
    
}
