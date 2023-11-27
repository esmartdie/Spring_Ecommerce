package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface IOrderStatusService {

    OrderStatus save(OrderStatus orderStatus);
    Optional<OrderStatus> findById(Integer id);
    List<OrderStatus> findAll();

    OrderStatus findByStatus(String status);
}
