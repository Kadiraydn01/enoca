package com.enoca.ecommerce.dto;

import com.enoca.ecommerce.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private long id;
    private String userName;

   
}
