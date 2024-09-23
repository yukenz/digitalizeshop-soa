package id.co.awan.digitalizeshopsoa.database.first.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "backend")
public class BackendModel {

    @Id
    private String id;
    private String secret;

}
