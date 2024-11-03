package com.enoca.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_detail_history", schema = "enoca")
public class OrderDetailHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price_at_purchase")
    private Double priceAtPurchase;  // Satın alınan andaki fiyat

    @Column(name = "quantity")
    private int quantity;  // Satın alınan miktar

    @Column(name = "total_price")
    private Double totalPrice;  // quantity * priceAtPurchase olarak hesaplanır
}
