
package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import runkoserver.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
    List<Person> findByName(String name);
}

