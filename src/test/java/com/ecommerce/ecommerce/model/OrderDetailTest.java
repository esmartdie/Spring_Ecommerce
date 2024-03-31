package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.ProductInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailTest {
    private OrderDetail orderDetail;

    @BeforeEach
    public void setUp() {
        orderDetail = new OrderDetail();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        orderDetail.setId(id);
        assertEquals(id, orderDetail.getId());
    }

    @Test
    public void testSetAndGetName() {
        String name = "Product ABC";
        orderDetail.setName(name);
        assertEquals(name, orderDetail.getName());
    }

    @Test
    public void testSetAndGetQuantity() {
        double quantity = 10.0;
        orderDetail.setQuantity(quantity);
        assertEquals(quantity, orderDetail.getQuantity());
    }

    @Test
    public void testSetAndGetPrice() {
        double price = 25.0;
        orderDetail.setPrice(price);
        assertEquals(price, orderDetail.getPrice());
    }

    @Test
    public void testSetAndGetTotal() {
        double total = 250.0;
        orderDetail.setTotal(total);
        assertEquals(total, orderDetail.getTotal());
    }

    @Test
    public void testSetAndGetOrder() {
        Order order = new Order();
        orderDetail.setOrder(order);
        assertEquals(order, orderDetail.getOrder());
    }

    @Test
    public void testSetAndGetProduct() {
        Product product = new Product();
        orderDetail.setProduct(product);
        assertEquals(product, orderDetail.getProduct());
    }

    @Test
    public void testSetAndGetInventory() {
        ProductInventory inventory = new ProductInventory();
        orderDetail.setInventory(inventory);
        assertEquals(inventory, orderDetail.getInventory());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String name = "Product ABC";
        double quantity = 10.0;
        double price = 25.0;
        double total = 250.0;

        orderDetail.setId(id);
        orderDetail.setName(name);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(price);
        orderDetail.setTotal(total);

        String expectedString = "OrderDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + total +
                '}';

        assertEquals(expectedString, orderDetail.toString());
    }
}
