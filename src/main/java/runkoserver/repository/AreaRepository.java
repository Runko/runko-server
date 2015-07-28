
package runkoserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import runkoserver.domain.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    
}
