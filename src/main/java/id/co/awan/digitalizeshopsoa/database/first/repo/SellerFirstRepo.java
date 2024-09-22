package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.domain.SellerFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

public interface SellerFirstRepo extends JpaRepository<SellerFirst, String> {

    @Procedure("authSeller")
    Boolean authSeller(String usernameIn, String passwordIn);
}
