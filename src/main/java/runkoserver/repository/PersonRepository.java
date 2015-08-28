
package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import runkoserver.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Long>{
    List<Person> findByName(String name);
    Person findById(Long id);
    Person findByUsername(String username);
}

