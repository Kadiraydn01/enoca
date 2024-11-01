package com.enoca.ecommerce.repository;

import com.enoca.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    public List<Order> findByCustomerId(@Param("customerId") long customerId);

}
