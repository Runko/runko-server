package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Content;
import runkoserver.domain.Element;
import runkoserver.domain.Person;

public interface ElementRepository extends JpaRepository<Element, Long> {

}
