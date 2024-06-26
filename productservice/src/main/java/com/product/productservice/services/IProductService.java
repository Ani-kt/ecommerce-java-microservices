package com.product.productservice.services;

import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    Product getSingleProduct(Long id);
    List<Product> getAllProducts();

    List<Product> getLimitedProduct(int limit);

    List<Product> getProductsInSortedOrder(String sort);

    List<Category> getAllCategory();

    List<Product> getProductInASpecificCategory(String category);
}
