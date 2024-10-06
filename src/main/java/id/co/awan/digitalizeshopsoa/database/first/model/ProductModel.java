package id.co.awan.digitalizeshopsoa.database.first.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
@Table(name = "product")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String seller;
    private String name;
    private BigInteger price;
    private String description;
    @Column(name = "image_uri")
    private String imageUri;
    private BigDecimal discount;
    private String category;
    private boolean isAvailable;



}
