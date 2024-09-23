package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.model.SellerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

public interface SellerModelRepo extends JpaRepository<SellerModel, String> {

    @Procedure("authSeller")
    Boolean authSeller(String usernameIn, String passwordIn);
}
