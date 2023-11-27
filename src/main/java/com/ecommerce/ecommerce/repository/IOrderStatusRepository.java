package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus,Integer> {

    OrderStatus findByStatus(String status);
}
