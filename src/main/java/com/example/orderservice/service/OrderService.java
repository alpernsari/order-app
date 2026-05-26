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
            Boolean isStockDeducted = stockServiceClient.post()
                    .uri("/{id}/deduct?quantity={quantity}", productId, quantity)
                    .retrieve()
                    .body(Boolean.class);
            if (Boolean.TRUE.equals(isStockDeducted)) {
                order.setStatus("SUCCESS");
            } else {
                order.setStatus("FAILED"); 
            }
        } catch (Exception e) {
            order.setStatus("FAILED_NETWORK_ERROR");
        }
        return orderRepository.save(order);
    }
}
