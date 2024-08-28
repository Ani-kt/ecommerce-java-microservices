package com.product.productservice.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonSerialize
public class ProductResponseDto implements Serializable {
    private Long id;
    private String title;
    private float price;
    private String description;
    private String category;
    private String image;
}
