package com.product.productservice.repositories;

import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product save(ProductRequestDto req);
    List<Product> findByCategoryIdIn(List<Long> categoryIds);
}
