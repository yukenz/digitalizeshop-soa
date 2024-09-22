package id.co.awan.digitalizeshopsoa.database.second.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seller")
public class SellerSecond {

    @Id
    public String id;

    public String name;
    public String username;

    public String password;
}
