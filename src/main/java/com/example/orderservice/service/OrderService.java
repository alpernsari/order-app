package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestClient stockServiceClient;

    public Order createOrder(Long productId, Integer quantity) {
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);

        try {
            // 1. ZURNANIN ZIRT DEDİĞİ YER: Ağ üzerinden Stok servisine HTTP POST isteği atıyoruz
            Boolean isStockDeducted = stockServiceClient.post()
                    .uri("/{id}/deduct?quantity={quantity}", productId, quantity)
                    .retrieve()
                    .body(Boolean.class);

            // 2. Stok servisinden gelen cevaba (True/False) göre sipariş durumunu belirliyoruz
            if (Boolean.TRUE.equals(isStockDeducted)) {
                order.setStatus("SUCCESS");
            } else {
                order.setStatus("FAILED"); // Stok yetersizse
            }
        } catch (Exception e) {
            // Eğer stok servisi çökmüşse veya ulaşılamıyorsa siparişi direkt reddet
            order.setStatus("FAILED_NETWORK_ERROR");
        }

        // 3. Siparişi kendi order_db veritabanımıza kaydediyoruz
        return orderRepository.save(order);
    }
}