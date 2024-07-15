package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderDetail;
import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.model.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        order.setId(id);
        assertEquals(id, order.getId());
    }

    @Test
    public void testSetAndGetNumber() {
        String number = "ORD123";
        order.setNumber(number);
        assertEquals(number, order.getNumber());
    }

    @Test
    public void testSetAndGetCreationDate() {
        Date creationDate = new Date();
        order.setCreationDate(creationDate);
        assertEquals(creationDate, order.getCreationDate());
    }

    @Test
    public void testSetAndGetReceptionDate() {
        Date receptionDate = new Date();
        order.setReceptionDate(receptionDate);
        assertEquals(receptionDate, order.getReceptionDate());
    }

    @Test
    public void testSetAndGetTotal() {
        double total = 100.0;
        order.setTotal(total);
        assertEquals(total, order.getTotal());
    }

    @Test
    public void testSetAndGetUser() {
        User user = new User();
        order.setUser(user);
        assertEquals(user, order.getUser());
    }

    @Test
    public void testSetAndGetDetails() {
        List<OrderDetail> details = new ArrayList<>();
        OrderDetail detail1 = new OrderDetail();
        details.add(detail1);
        order.setDetails(details);
        assertEquals(details, order.getDetails());
    }

    @Test
    public void testSetAndGetStatus() {
        OrderStatus status = new OrderStatus();
        order.setStatus(status);
        assertEquals(status, order.getStatus());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String number = "ORD123";
        Date creationDate = new Date();
        Date receptionDate = new Date();
        double total = 100.0;

        order.setId(id);
        order.setNumber(number);
        order.setCreationDate(creationDate);
        order.setReceptionDate(receptionDate);
        order.setTotal(total);

        String expectedString = "Order{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", receptionDate=" + receptionDate +
                ", total=" + total +
                '}';

        assertEquals(expectedString, order.toString());
    }

    @Test
    public void testOrderRelations() {

        User user = new User();
        OrderDetail detail = new OrderDetail();
        OrderStatus status = new OrderStatus();

        order.setUser(user);
        order.setStatus(status);
        List<OrderDetail> details = new ArrayList<>();
        details.add(detail);
        order.setDetails(details);

        assertEquals(user, order.getUser());
        assertEquals(status, order.getStatus());
        assertEquals(details, order.getDetails());
    }
}
