package com.product.productservice.controlleradvice;

import com.product.productservice.dtos.ProductResponseMessageDto;
import com.product.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductResponseMessageDto> handleInvalidProd(){
        return new ResponseEntity<>(new ProductResponseMessageDto
                (null,"Product Doesn't Exist from ControllerAdvice!!"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ProductResponseMessageDto> handleArithmeticException(){
        return new ResponseEntity<>(new ProductResponseMessageDto
                (null,"Something went wrong from ControllerAdvice"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
