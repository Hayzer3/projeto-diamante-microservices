package br.com.diamante.order.service;

import br.com.diamante.order.exception.OutOfStockException;
import br.com.diamante.order.exception.PaymentFailedException;
import br.com.diamante.order.exception.ResourceNotFoundException;
import br.com.diamante.order.model.CustomerOrder;
import br.com.diamante.order.model.Dish;
import br.com.diamante.order.repository.DishRepository;
import br.com.diamante.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final DishRepository dishes;
    private final OrderRepository orders;
    private final PaymentClient payments;

    public OrderService(DishRepository dishes, OrderRepository orders, PaymentClient payments) {
        this.dishes = dishes;
        this.orders = orders;
        this.payments = payments;
    }

    @Transactional
    public CustomerOrder place(Long dishId, int quantity) {
        Dish dish = dishes.findLockedById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        if (dish.getStock() < quantity) {
            throw new OutOfStockException("Dish out of stock");
        }
        dish.setStock(dish.getStock() - quantity);
        BigDecimal total = dish.getPrice().multiply(BigDecimal.valueOf(quantity));

        Map<?, ?> payment;
        try {
            payment = payments.charge(total);
        } catch (RuntimeException ex) {
            throw new PaymentFailedException("Payment failed after retries", ex);
        }

        if (payment == null || !"APPROVED".equals(payment.get("status"))) {
            throw new PaymentFailedException("Payment was not approved", null);
        }

        return orders.save(new CustomerOrder(dishId, quantity, total));
    }
}
