package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.domain.BackendEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackendEntityRepo extends JpaRepository<BackendEntity, String> {

    // Caching
    @Override
    @Cacheable("banckendEntity")
    Optional<BackendEntity> findById(String id);
}
