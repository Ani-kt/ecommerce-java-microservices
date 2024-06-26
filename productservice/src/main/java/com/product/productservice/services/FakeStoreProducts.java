package com.product.productservice.services;

import com.product.productservice.configurations.ApiConfig;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FakeStoreProducts implements IProductService{
    ApiConfig apiConfig;
    RestTemplate restTemplate;
    @Autowired
    public FakeStoreProducts(ApiConfig apiConfig,RestTemplate restTemplate) {
        this.apiConfig=apiConfig;
        this.restTemplate=restTemplate;
    }
    @Override
    public Product getSingleProduct(Long id) {
        ProductResponseDto res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl()+id, ProductResponseDto.class);
        return mapToProducts(res);
    }

    private Product mapToProducts(ProductResponseDto res) {
        Product product=new Product();
        product.setId(res.getId());
        product.setName(res.getTitle());
        product.setCategory(new Category());
        product.getCategory().setName(res.getCategory());
        product.setImage(res.getImage());
        product.setPrice(res.getPrice());
        product.setDescription(res.getDescription());
        return product;
    }
    @Override
    public List<Product> getAllProducts() {
        ProductResponseDto[] res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl(), ProductResponseDto[].class);
        List<Product> productList=new ArrayList<>();
        for(ProductResponseDto productResponseDto:res){
            productList.add(mapToProducts(productResponseDto));
        }
        return productList;
    }

    @Override
    public List<Product> getLimitedProduct(int limit) {
        List<Product> productList=getAllProducts();
        List<Product> limitedItems=new ArrayList<>();
        for(int i=0;i<limit;i++){
            limitedItems.add(productList.get(i));
        }
        return limitedItems;
    }

    @Override
    public List<Product> getProductsInSortedOrder(String sort) {
        List<Product> productList=getAllProducts();
        if("asc".equalsIgnoreCase(sort)){
            productList.sort(Comparator.comparing(Product::getId));
        }else if("desc".equalsIgnoreCase(sort)){
            productList.sort(Comparator.comparing(Product::getId).reversed());
        }
        return productList;
    }

    @Override
    public List<Category> getAllCategory() {
        String[] res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl()+"categories",String[].class);
        List<Category> categoryList=new ArrayList<>();
        long count=0;
        for(String r:res){
            count++;
            categoryList.add(mapToCategory(r,count));
        }
        return categoryList;
    }

    @Override
    public List<Product> getProductInASpecificCategory(String category) {
        System.out.println(category);
        ProductResponseDto[] res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl()+"category/"+category,ProductResponseDto[].class);
        List<Product> productList=new ArrayList<>();
        for(ProductResponseDto r:res){
            System.out.println("r category "+r.getCategory()+", category "+category);
            if(category.equalsIgnoreCase(r.getCategory())){
                productList.add(mapToProducts(r));
            }
        }
        return productList;
    }

    private Category mapToCategory(String res,Long id) {
        Category category=new Category();
        category.setId(id);
        category.setName(res);
        return category;
    }
}
