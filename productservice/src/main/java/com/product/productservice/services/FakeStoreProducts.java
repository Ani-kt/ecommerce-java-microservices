package com.product.productservice.services;

import com.product.productservice.configurations.ApiConfig;
import com.product.productservice.dtos.ProductRequestDto;
import com.product.productservice.dtos.ProductResponseDto;
import com.product.productservice.exceptions.CategoryNotPresentException;
import com.product.productservice.exceptions.ProductNotFoundException;
import com.product.productservice.models.Category;
import com.product.productservice.models.Product;
import com.product.productservice.repositories.CategoryRepository;
import com.product.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class FakeStoreProducts implements IProductService{
    ApiConfig apiConfig;
    RestTemplate restTemplate;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    RedisTemplate redisTemplate;
    @Autowired
    public FakeStoreProducts(ApiConfig apiConfig,RestTemplate restTemplate,ProductRepository productRepository,CategoryRepository categoryRepository,
                             RedisTemplate redisTemplate) {
        this.apiConfig=apiConfig;
        this.restTemplate=restTemplate;
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;
        this.redisTemplate=redisTemplate;
    }
    @Override
    public Product getSingleProduct(Long id) throws ProductNotFoundException {
        if(redisTemplate.opsForHash().hasKey(id,"getSingleProd")==false) {
            if (id > 20 && id < 40) {
                throw new ProductNotFoundException();
            }
            if (id > 40) {
                throw new ArithmeticException();
            }
            ProductResponseDto res = restTemplate.getForObject(apiConfig.getFakeStoreApiUrl() + id, ProductResponseDto.class);
            redisTemplate.opsForHash().put(id, "getSingleProd", resmapToProducts(res));

            // Map the response and save it in the Redis cache with TTL
            redisTemplate.opsForValue().set(id.toString(), resmapToProducts(res), Duration.ofMinutes(5));
        }
        return (Product) redisTemplate.opsForHash().get(id,"getSingleProd");
    }

    private Product resmapToProducts(ProductResponseDto res) {
        Product product=new Product();
        product.setId(res.getId());
        product.setName(res.getTitle());
        product.setCategory(new Category());
        product.getCategory().setName(res.getCategory());
        product.setImage(res.getImage());
        product.setPrice(res.getPrice());
        product.setDescr(res.getDescription());
        return product;
    }
    @Override
    public List<Product> getAllProducts() {
        ProductResponseDto[] res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl(), ProductResponseDto[].class);
        List<Product> productList=new ArrayList<>();
        for(ProductResponseDto productResponseDto:res){
            productList.add(resmapToProducts(productResponseDto));
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
    public List<Product> getProductInASpecificCategory(String category) throws CategoryNotPresentException {
        ProductResponseDto[] res=restTemplate.getForObject(apiConfig.getFakeStoreApiUrl()+"category/"+category,ProductResponseDto[].class);
        List<Product> productList=new ArrayList<>();
        for(ProductResponseDto r:res){
            if(category.equalsIgnoreCase(r.getCategory())){
                productList.add(resmapToProducts(r));
            }
        }
        if(productList.isEmpty()){
            throw new CategoryNotPresentException();
        }
        return productList;
    }

    @Override
    public Boolean saveProduct(ProductRequestDto req) {
        Product p=reqDtoToProduct(req);
        Optional<Category> category=categoryRepository.findByName(req.getCategory());
        if(category.isEmpty()){
            categoryRepository.save(p.getCategory());
        }else{
            p.setCategory(category.get());
        }
        Product res=productRepository.save(p);
        if(p==null){
            return false;
        }
        productMapToProductResponseDto(res);
        return true;
    }

    @Override
    public List<ProductResponseDto> getProductByCategoryId(List<Long> categoryId) throws ProductNotFoundException {
        List<Product> p=productRepository.findByCategoryIdIn(categoryId);
        if(p.isEmpty()){
            throw new ProductNotFoundException();
        }
        List<ProductResponseDto> productResponseDtos=new ArrayList<>();
        for(Product product:p){
            productResponseDtos.add(productMapToProductResponseDto(product));
        }
        return productResponseDtos;
    }

    private Product reqDtoToProduct(ProductRequestDto req) {
        Product product=new Product();
        product.setName(req.getTitle());
        product.setCategory(new Category());
        product.getCategory().setName(req.getCategory());
        product.setImage(req.getImage());
        product.setPrice(req.getPrice());
        product.setDescr(req.getDescription());
        return product;
    }
    private ProductResponseDto productMapToProductResponseDto(Product product) {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(product.getId());
        response.setTitle(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescr());
        response.setCategory(product.getCategory().getName());
        response.setImage(product.getImage());
        return response;
    }

    private Category mapToCategory(String res,Long id) {
        Category category=new Category();
        category.setId(id);
        category.setName(res);
        return category;
    }

}
