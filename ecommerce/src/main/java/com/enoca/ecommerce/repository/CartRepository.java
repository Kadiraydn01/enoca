package com.enoca.ecommerce.repository;

import com.enoca.ecommerce.entity.Cart;
import com.enoca.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM Cart c WHERE c.customer.id = :customerId")
    public Cart findCartByCustomerId(@Param("customerId") long customerId);
}
