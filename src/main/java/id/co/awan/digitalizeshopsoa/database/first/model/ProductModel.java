package id.co.awan.digitalizeshopsoa.database.first.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
@Table(name = "product")
public class ProductModel {

    @Id
    private Integer id;

    @Column(name = "seller_id")
    private String sellerId;
    private String name;
    private BigInteger price;
    private String description;
    @Column(name = "image_uri")
    private String imageUri;
    private BigDecimal discount;
    private String category;
    private boolean isAvailable;

}
