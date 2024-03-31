package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusTest {
    private OrderStatus orderStatus;

    @BeforeEach
    public void setUp() {
        orderStatus = new OrderStatus();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        orderStatus.setId(id);
        assertEquals(id, orderStatus.getId());
    }

    @Test
    public void testSetAndGetStatus() {
        String status = "Pending";
        orderStatus.setStatus(status);
        assertEquals(status, orderStatus.getStatus());
    }

    @Test
    public void testSetAndGetOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        Order order2 = new Order();
        orders.add(order1);
        orders.add(order2);
        orderStatus.setOrders(orders);
        assertEquals(orders, orderStatus.getOrders());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String status = "Pending";
        List<Order> orders = new ArrayList<>();

        orderStatus.setId(id);
        orderStatus.setStatus(status);
        orderStatus.setOrders(orders);

        String expectedString = "OrderStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", orders=" + orders +
                '}';

        assertEquals(expectedString, orderStatus.toString());
    }
}
