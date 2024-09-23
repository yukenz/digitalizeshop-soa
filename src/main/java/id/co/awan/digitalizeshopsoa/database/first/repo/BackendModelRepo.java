package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.model.BackendModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackendModelRepo extends JpaRepository<BackendModel, String> {

    // Caching
    @Override
    @Cacheable("banckendEntity")
    Optional<BackendModel> findById(String id);
}
