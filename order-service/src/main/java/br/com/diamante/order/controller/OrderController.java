package br.com.diamante.order.controller;

import br.com.diamante.order.dto.AssistantQuestion;
import br.com.diamante.order.dto.CreateOrder;
import br.com.diamante.order.dto.CreateReview;
import br.com.diamante.order.exception.ResourceNotFoundException;
import br.com.diamante.order.exception.TooManyRequestsException;
import br.com.diamante.order.model.CustomerOrder;
import br.com.diamante.order.model.Dish;
import br.com.diamante.order.repository.DishRepository;
import br.com.diamante.order.repository.OrderRepository;
import br.com.diamante.order.service.ChatService;
import br.com.diamante.order.service.OrderService;
import br.com.diamante.order.service.RequestRateLimiter;
import br.com.diamante.order.service.ReviewPublisher;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class OrderController {
    private final DishRepository dishes;
    private final OrderRepository orders;
    private final OrderService orderService;
    private final ReviewPublisher reviewPublisher;
    private final ChatService chatService;
    private final RequestRateLimiter rateLimiter = new RequestRateLimiter(20);

    public OrderController(DishRepository dishes, OrderRepository orders, OrderService orderService,
                           ReviewPublisher reviewPublisher, ChatService chatService) {
        this.dishes = dishes;
        this.orders = orders;
        this.orderService = orderService;
        this.reviewPublisher = reviewPublisher;
        this.chatService = chatService;
    }

    @GetMapping("/dishes")
    public Iterable<Dish> allDishes() {
        return dishes.findAll();
    }

    @GetMapping("/dishes/{id}")
    public Dish dish(@PathVariable Long id) {
        return dishes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
    }

    @PostMapping("/orders")
    public ResponseEntity<CustomerOrder> createOrder(@Valid @RequestBody CreateOrder request) {
        if (!rateLimiter.tryAcquire()) {
            throw new TooManyRequestsException();
        }
        CustomerOrder order = orderService.place(request.dishId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/orders/{id}")
    public CustomerOrder order(@PathVariable Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @PostMapping("/reviews")
    public ResponseEntity<Void> review(@Valid @RequestBody CreateReview request) {
        reviewPublisher.publish(request.dishId(), request.rating(), request.comment());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/assistant")
    public Map<String, String> assistant(@Valid @RequestBody AssistantQuestion request) {
        return Map.of("answer", chatService.answer(request.question()));
    }
}
