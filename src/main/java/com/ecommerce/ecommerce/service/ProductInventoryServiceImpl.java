package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import com.ecommerce.ecommerce.repository.IProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductInventoryServiceImpl implements IProductInventoryService{

    private final IProductInventoryRepository productInventoryRepository;
    private final IProductService productService;

    @Autowired
    public ProductInventoryServiceImpl(IProductInventoryRepository productInventoryRepository, IProductService productService) {
        this.productInventoryRepository = productInventoryRepository;
        this.productService = productService;
    }

    @Override
    public ProductInventory save(ProductInventory inventory) {
        return productInventoryRepository.save(inventory);
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

    @Override
    public void newOrderProductInventoryLog(OrderDetail od, Order order, Date logDate) {
        Product product = od.getProduct();
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProduct(product);

        if (!productInventoryList.isEmpty()) {
            productInventoryList.sort(Comparator.comparing(ProductInventory::getId).reversed());
            ProductInventory latestInventory = productInventoryList.get(0);

            int initialExistence = latestInventory.getFinalQuantity();

            ProductInventory pI = new ProductInventory();
            pI.setDate(logDate);
            pI.setOperationName("Order created: " + order.getNumber());
            pI.setInitialQuantity(initialExistence);
            pI.setOperationQuantity((int) od.getQuantity());
            pI.setFinalQuantity(pI.getInitialQuantity() - pI.getOperationQuantity());
            pI.setProduct(product);
            pI.setDetails(od);

            productInventoryRepository.save(pI);

            product.setQuantity(pI.getFinalQuantity());
            productService.update(product);
        }
    }
    @Override
    public void newProductAddedProductInventoryLog(Product product, Date logDate) {
        ProductInventory pI = new ProductInventory();
        pI.setDate(logDate);
        pI.setOperationName("Product Available to Sale");
        pI.setInitialQuantity(0);
        pI.setOperationQuantity(product.getQuantity());
        pI.setFinalQuantity(pI.getInitialQuantity() + pI.getOperationQuantity());
        pI.setProduct(product);

        productInventoryRepository.save(pI);
    }

    @Override
    public void updateProductInventoryFinalExistence(Product product, Date logDate) {
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProduct(product);

        if (!productInventoryList.isEmpty()) {
            productInventoryList.sort(Comparator.comparing(ProductInventory::getId).reversed());
            ProductInventory latestInventory = productInventoryList.get(0);

            int initialExistence = latestInventory.getFinalQuantity();

            ProductInventory pI = new ProductInventory();
            pI.setDate(logDate);
            pI.setOperationName("Admin update final quantity by GUI");
            pI.setInitialQuantity(initialExistence);
            pI.setFinalQuantity(product.getQuantity());
            pI.setOperationQuantity(pI.getFinalQuantity() - pI.getInitialQuantity());
            pI.setProduct(product);

            productInventoryRepository.save(pI);
        }
    }

}
