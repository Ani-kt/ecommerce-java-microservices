package com.product.productservice.controllers;
import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import com.product.productservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    IProductService iProductService;
    @Autowired
    public ProductController(IProductService iProductService){
        this.iProductService=iProductService;
    }
    @GetMapping("/productlist")
    public List<Product> getAllProducts(){
        return iProductService.getAllProducts();

    }
    @GetMapping("/{id}")
    public Product getSingleProduct(@PathVariable Long id){
        return iProductService.getSingleProduct(id);

    }
    @GetMapping("")
    public List<Product> getLimitedProduct(@RequestParam int limit){
        return iProductService.getLimitedProduct(limit);

    }
    @GetMapping("/mode")
    public List<Product> getSortedProduct(@RequestParam String sort){
        return iProductService.getProductsInSortedOrder(sort);

    }
    @GetMapping("/categories")
    public List<Category> getAllCategory(){
        return iProductService.getAllCategory();

    }
    @GetMapping("/category/{category}")
    public List<Product> getProductInASpecificCategory(@PathVariable String category){
        return iProductService.getProductInASpecificCategory(category);

    }
    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductRequestDto req){
        return new Product();
    }
    @PatchMapping("/products/{id}")
    public Product updateProduct(@PathVariable Long id,@RequestBody ProductRequestDto req){
        return new Product();
    }
    @DeleteMapping("/products/{id}")
    public boolean deleteProduct(@PathVariable Long id){
        return true;
    }
}
