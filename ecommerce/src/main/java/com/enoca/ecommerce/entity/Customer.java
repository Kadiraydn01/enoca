package com.enoca.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer", schema = "enoca")
public class Customer extends BaseEntity {


    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade =CascadeType.ALL, mappedBy = "customer")
    @JsonManagedReference
    private Cart cart;


    @OneToMany(cascade =CascadeType.ALL, mappedBy = "customer")
    @JsonManagedReference
    private List<Order> orders;
   

    

}
