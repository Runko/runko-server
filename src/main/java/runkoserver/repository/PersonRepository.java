package runkoserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Person;

/**
 * Database of Runko's userinformation
 */
public interface PersonRepository extends JpaRepository<Person, Long>{
    @Query
    Person findById(Long id);
    
    @Query
    Person findByUsername(String username);
}
