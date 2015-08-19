package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Element;
import runkoserver.domain.Person;

/**
 * Database for elements.
 */
public interface ElementRepository extends JpaRepository<Element, Long>{
    
    @Query
    public List<Element> findByName(String name);
    
    @Query
    public List<Element> findByOwner(Person person);
}
