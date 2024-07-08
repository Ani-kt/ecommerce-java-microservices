package com.product.productservice.services;

import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.exceptions.CategoryNotPresentException;
import com.product.productservice.exceptions.ProductNotFoundException;
import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    Product getSingleProduct(Long id) throws ProductNotFoundException;
    List<Product> getAllProducts();

    List<Product> getLimitedProduct(int limit);

    List<Product> getProductsInSortedOrder(String sort);

    List<Category> getAllCategory();

    List<Product> getProductInASpecificCategory(String category) throws CategoryNotPresentException;

    Boolean saveProduct(ProductRequestDto req);

    List<ProductResponseDto> getProductByCategoryId(List<Long> id) throws ProductNotFoundException;
}
