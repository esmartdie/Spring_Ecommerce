package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Product save(Product product);
    Optional<Product> getProduct (Integer id);
    void update(Product product);
    void delete(Integer id);
    List<Product> findAll();
    List<Product> findAllInactiveProducts();
    List<Product> findAllActiveProducts();
}
