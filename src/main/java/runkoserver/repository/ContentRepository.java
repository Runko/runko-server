
package runkoserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import runkoserver.domain.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
    
}
