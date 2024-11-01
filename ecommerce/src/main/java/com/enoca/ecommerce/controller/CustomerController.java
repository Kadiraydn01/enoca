package com.enoca.ecommerce.controller;
import com.enoca.ecommerce.dto.CustomerResponse;
import com.enoca.ecommerce.entity.Customer;
import com.enoca.ecommerce.service.CustomerService;
import com.enoca.ecommerce.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    
    @PostMapping("/add")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.addCustomer(customer);
        if (createdCustomer != null) {
            CustomerResponse response = DtoConverter.convertToCustomerResponse(createdCustomer);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable long id) {
        Customer customer = customerService.getCustomer(id);
        if (customer != null) {
            CustomerResponse response = DtoConverter.convertToCustomerResponse(customer);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/update")
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(customer);
        if (updatedCustomer != null) {
            CustomerResponse response = DtoConverter.convertToCustomerResponse(updatedCustomer);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
    }
}
