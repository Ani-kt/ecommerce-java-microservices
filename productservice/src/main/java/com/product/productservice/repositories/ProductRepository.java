package com.product.productservice.repositories;

import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product save(ProductRequestDto req);
}
