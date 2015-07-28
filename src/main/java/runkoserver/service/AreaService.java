package runkoserver.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runkoserver.domain.Area;
import runkoserver.repository.AreaRepository;

@Service
public class AreaService implements RepoService {

    @Autowired
    AreaRepository repository;

    public boolean save(Area area) {
        String name = area.getName();
        if (name != null && !name.trim().isEmpty()) {
            repository.save(area);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        if (repository.exists(id)) {
            repository.delete(id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    public List<Area> findAll() {
        return repository.findAll();
    }
    
    public Area findById(Long id) {
        return repository.findOne(id);
    }
}
