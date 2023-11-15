package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;

import java.util.List;
import java.util.Optional;


public interface IOrderService {

    Order save (Order order);

    List<Order> findAll();

    String generateOrderIdentification();

    List<Order> findByUser(User user);

    Optional <Order> findById(Integer id);
}
