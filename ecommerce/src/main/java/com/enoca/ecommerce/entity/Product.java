package com.enoca.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", schema = "enoca")
public class Product extends BaseEntity {



    @Column(name = "product_name")
    private String name;


    @Column(name = "product_stock")
    private Long stock;


    @Column(name = "product_price")
    private Double price;


    @Column(name = "product_description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity = 0;


    @Column(name = "product_image")
    private String image;


    @ManyToOne(cascade ={CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "costumer_cart")
    @JsonBackReference
    private Cart cart;

}
