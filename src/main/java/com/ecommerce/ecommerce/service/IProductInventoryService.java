package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductInventoryService {

    ProductInventory save (ProductInventory inventory);
    Optional<ProductInventory> findById(Integer id);
    List<ProductInventory> findAll();
    List<ProductInventory> findByProduct(Product product);
    ProductInventory findLastProduct(Product product);

}
