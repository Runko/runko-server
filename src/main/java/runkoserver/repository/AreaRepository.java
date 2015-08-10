package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import runkoserver.domain.Area;
import runkoserver.domain.content.Content;

public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query
    public List<Area> findByVisibilityTrue();
    
    @Query
    public Area findByName(String name);
}
