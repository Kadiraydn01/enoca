package com.enoca.ecommerce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderErrorResponse {
    private int status;
    private String message;
    private  long timestamp;
}
