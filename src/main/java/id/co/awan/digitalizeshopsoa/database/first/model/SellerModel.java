package id.co.awan.digitalizeshopsoa.database.first.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "seller")
@Data
@NamedStoredProcedureQuery(name = "SellerFirst.authSeller", procedureName = "authSeller", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "usernameIn", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "passwordIn", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "isValid", type = Boolean.class)
})
public class SellerModel {

    @Id
    private String username;
    private String password;
    private String name;
    private String ownerName;
    private String address1;
    private String address2;
    private Boolean status;
    private String imageURI;
    private Date registrationDate;
    private Date lastLogin;

}
