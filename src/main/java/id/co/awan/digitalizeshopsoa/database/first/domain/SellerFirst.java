package id.co.awan.digitalizeshopsoa.database.first.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seller")
@Data
@NamedStoredProcedureQuery(name = "SellerFirst.authSeller", procedureName = "authSeller", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "usernameIn", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "passwordIn", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "isValid", type = Boolean.class)
})
public class SellerFirst {

    @Id
    private String id;

    private String name;
    private String username;

    private String password;

}
