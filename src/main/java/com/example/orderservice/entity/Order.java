package com.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DİKKAT: Bu artık mikroservis dünyasında fiziksel bir "Foreign Key" DEĞİL.
    // Sadece diğer servisteki bir ürünün ID'sini referans olarak tutan düz bir sayı.
    private Long productId;

    private Integer quantity;

    private String status;

    private LocalDateTime orderDate;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}