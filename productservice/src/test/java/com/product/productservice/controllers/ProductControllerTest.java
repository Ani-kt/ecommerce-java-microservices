package com.product.productservice.controllers;

import com.product.productservice.dtos.ProductResponseMessageDto;
import com.product.productservice.exceptions.ProductNotFoundException;
import com.product.productservice.models.Product;
import com.product.productservice.services.IProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProductControllerTest {
    @Autowired
    ProductController productController;
    @MockBean
    IProductService productService;

    @Test
    public void getAllProductsTest(){
        //Arrange
        Product p1=new Product();
        p1.setName("Appo");
        Product p2=new Product();
        p2.setName("Appo V1");
        Product p3=new Product();
        p3.setName("Iphone");

        List<Product> expectedProductList= Arrays.asList(p1,p2,p3);
        Mockito.when(productService.getAllProducts()).thenReturn(expectedProductList);

        //Act
        List<Product> actualProductList=productController.getAllProducts();

        //Assert
        Assertions.assertTrue(actualProductList.size()==3);
    }

    @Test
    public void getSingleProductTest() throws ProductNotFoundException {
        //Arrange
        Mockito.when(productService.getSingleProduct(25L))
                .thenThrow(ProductNotFoundException.class);

        //Act
        ResponseEntity<ProductResponseMessageDto> actual=productController.getSingleProduct(25L);

        //Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND,actual.getStatusCode());
    }

}