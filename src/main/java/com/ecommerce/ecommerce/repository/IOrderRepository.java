package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);

    Optional<Order> findByNumber(String number);
}
