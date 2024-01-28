package com.ecommerce.ecommerce.repository;


import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductInventoryRepository extends JpaRepository<ProductInventory, Integer> {

    List<ProductInventory> findByProduct(Product product);
}
