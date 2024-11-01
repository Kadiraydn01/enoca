package com.enoca.ecommerce.service;

import com.enoca.ecommerce.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    List<Customer> listCustomers();
    Customer getCustomer(Long id);
    Customer updateCustomer(Customer customer);
    Customer deleteCustomer(Long id); 
    
}
