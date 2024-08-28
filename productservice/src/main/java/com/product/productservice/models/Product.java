package com.product.productservice.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonSerialize
public class Product extends BaseModel implements Serializable {
    private String descr;
    private String image;
    private float price;
    @ManyToOne
    private Category category;

}
