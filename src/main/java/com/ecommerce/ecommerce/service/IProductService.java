package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    public Product save(Product product);
    public Optional<Product> getProduct (Integer id);
    public void update(Product product);
    public void delete(Integer id);
    public List<Product> findAll();

}
