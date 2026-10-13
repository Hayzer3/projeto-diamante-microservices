package br.com.diamante.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dishId;
    private int quantity;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;

    protected CustomerOrder() {
    }

    public CustomerOrder(Long dishId, int quantity, BigDecimal totalPrice) {
        this.dishId = dishId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getDishId() {
        return dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
