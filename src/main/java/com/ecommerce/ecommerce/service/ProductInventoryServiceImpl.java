package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.repository.IProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductInventoryServiceImpl implements IProductInventoryService{


    @Override
    public ProductInventory save(ProductInventory inventory) {
        return productInventoryRepository.save(inventory);
    }

    @Override
    public Optional<ProductInventory> findById(Integer id) {
        return productInventoryRepository.findById(id);
    }

    @Override
    public List<ProductInventory> findAll() {
        return productInventoryRepository.findAll();
    }

    @Override
    public List<ProductInventory> findByProduct(Product product) {
        return productInventoryRepository.findByProduct(product);
    }
    @Override
    public ProductInventory findLastProduct (Product product){

        List<ProductInventory> productInventoryList = productInventoryRepository.findByProduct(product);

        productInventoryList.sort(Comparator.comparing(ProductInventory::getId).reversed());

        return productInventoryList.get(0);

    }

    @Autowired
    private IProductInventoryRepository productInventoryRepository;

}
