package br.com.diamante.order.service;

import br.com.diamante.order.dto.ReviewMessage;
import br.com.diamante.order.exception.ResourceNotFoundException;
import br.com.diamante.order.model.Dish;
import br.com.diamante.order.repository.DishRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewPublisher {
    public static final String EXCHANGE = "delivery.exchange";
    public static final String ROUTING_KEY = "reviews.new";
    private final RabbitTemplate rabbitTemplate;
    private final DishRepository dishes;

    public ReviewPublisher(RabbitTemplate rabbitTemplate, DishRepository dishes) {
        this.rabbitTemplate = rabbitTemplate;
        this.dishes = dishes;
    }

    public void publish(Long dishId, int rating, String comment) {
        Dish dish = dishes.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
        ReviewMessage message = new ReviewMessage(dishId, dish.getName(), rating, comment);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }
}
