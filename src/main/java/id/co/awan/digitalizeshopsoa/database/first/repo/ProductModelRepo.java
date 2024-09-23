package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.model.BackendModel;
import id.co.awan.digitalizeshopsoa.database.first.model.ProductModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductModelRepo extends JpaRepository<ProductModel, Integer> {

}
