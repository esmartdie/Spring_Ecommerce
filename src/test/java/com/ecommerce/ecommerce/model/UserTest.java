package com.ecommerce.ecommerce.model;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testSetAndGetId() {
        Integer id = 1;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testSetAndGetName() {
        String name = "John Doe";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    public void testSetAndGetUsername() {
        String username = "johndoe";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        String email = "john.doe@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetAndGetAddress() {
        String address = "123 Main St, City, Country";
        user.setAddress(address);
        assertEquals(address, user.getAddress());
    }

    @Test
    public void testSetAndGetTelephone() {
        String telephone = "123-456-7890";
        user.setTelephone(telephone);
        assertEquals(telephone, user.getTelephone());
    }

    @Test
    public void testSetAndGetUserRol() {
        String userRol = "customer";
        user.setUserRol(userRol);
        assertEquals(userRol, user.getUserRol());
    }

    @Test
    public void testSetAndGetPassword() {
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetAndGetProducts() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        products.add(product1);
        products.add(product2);
        user.setProducts(products);
        assertEquals(products, user.getProducts());
    }

    @Test
    public void testSetAndGetOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        Order order2 = new Order();
        orders.add(order1);
        orders.add(order2);
        user.setOrders(orders);
        assertEquals(orders, user.getOrders());
    }

    @Test
    public void testToString() {
        Integer id = 1;
        String name = "John Doe";
        String username = "johndoe";
        String email = "john.doe@example.com";
        String address = "123 Main St, City, Country";
        String telephone = "123-456-7890";
        String userRol = "customer";
        String password = "password123";

        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setAddress(address);
        user.setTelephone(telephone);
        user.setUserRol(userRol);
        user.setPassword(password);

        String expectedString = "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", userRol='" + userRol + '\'' +
                ", password='" + password + '\'' +
                '}';

        assertEquals(expectedString, user.toString());
    }
}
