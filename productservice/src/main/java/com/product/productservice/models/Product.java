package com.product.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@Entity
public class Product extends BaseModel {
    private String description;
    private String image;
    private float price;
    @ManyToOne
    private Category category;

}
