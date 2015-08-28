package runkoserver.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import runkoserver.domain.Area;
import runkoserver.domain.Person;

public interface AreaRepository extends JpaRepository<Area, Long> {

    @Query
    public List<Area> findByVisibilityTrue();
    
    @Query
    public Area findByName(String name);
    
    @Query
    public List<Area> findByOwner(Person person);
}
