package com.product.productservice.controllers;
import com.product.productservice.ProductserviceApplication;
import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.dtos.ProductResponseMessageDto;
import com.product.productservice.exceptions.CategoryNotPresentException;
import com.product.productservice.exceptions.ProductNotFoundException;
import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import com.product.productservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<ProductResponseMessageDto> getSingleProduct(@PathVariable Long id){
        Product product;
        try {
            product=iProductService.getSingleProduct(id);
        }catch(ProductNotFoundException e){
            return new ResponseEntity<>(new ProductResponseMessageDto(null,"Product Doesn't Exist !!"), HttpStatus.NOT_FOUND);
        }catch (ArithmeticException e){
            return new ResponseEntity<>(new ProductResponseMessageDto(null,"Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ProductResponseMessageDto(product,"Product Exist"),HttpStatus.OK);
    }
    @GetMapping("exception/{id}")
    public ResponseEntity<ProductResponseMessageDto> getSingleProductException(@PathVariable Long id) throws ProductNotFoundException {
        Product product;
        product=iProductService.getSingleProduct(id);
        return new ResponseEntity<>(new ProductResponseMessageDto(product,"Product Exist"),HttpStatus.OK);
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
    public ResponseEntity<ProductResponseMessageDto> getProductInASpecificCategory(@PathVariable String category){
        List<Product> productList;
        try{
            productList=iProductService.getProductInASpecificCategory(category);
        }catch (CategoryNotPresentException e){
            List<String> categoryName=new ArrayList<>();
            for(Category c:getAllCategory()){
                categoryName.add(c.getName());
            }
            String message = "Category Not Available. Please select any one from " + categoryName;
            return new ResponseEntity<>(
                    new ProductResponseMessageDto(null,message),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ProductResponseMessageDto(productList,"SUCCESS"),HttpStatus.OK);
    }
    @PostMapping("/store-products")
    public String addProduct(@RequestBody ProductRequestDto req){
        Boolean flag=iProductService.saveProduct(req);
        if(flag){
            return "SUCCESS";
        }else{
            return "FAILURE";
        }
    }
    @GetMapping("/byCategoryId")
    public ResponseEntity<ProductResponseMessageDto> getProductByCategoryId(@RequestParam("categoryIds") List<Long> id) throws ProductNotFoundException {
        List<ProductResponseDto> products = iProductService.getProductByCategoryId(id);
        return new ResponseEntity<>(new ProductResponseMessageDto(products,"SUCCESS"),HttpStatus.OK);
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
