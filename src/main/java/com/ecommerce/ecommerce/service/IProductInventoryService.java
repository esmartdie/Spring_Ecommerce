package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductInventoryService {
    ProductInventory save (ProductInventory inventory);
    List<ProductInventory> findByProduct(Product product);
    ProductInventory findLastProduct(Product product);
    void newOrderProductInventoryLog(OrderDetail od, Order order, Date logDate);
    void newProductAddedProductInventoryLog(Product product, Date logDate);
    void updateProductInventoryFinalExistence(Product product, Date logDate);

}
