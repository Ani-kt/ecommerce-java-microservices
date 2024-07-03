package com.product.productservice.dtos;

import com.product.productservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseMessageDto<T> {
    Object obj;
    String message;
}
