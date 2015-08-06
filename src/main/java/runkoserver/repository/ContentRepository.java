
package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.Person;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query
    public List<Content> findByOwner(Person person);
}
