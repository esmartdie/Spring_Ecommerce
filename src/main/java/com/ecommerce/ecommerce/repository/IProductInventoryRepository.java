package com.ecommerce.ecommerce.repository;


import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface IProductInventoryRepository extends JpaRepository<ProductInventory, Integer> {

    List<ProductInventory> findByProduct(Product product);
}
