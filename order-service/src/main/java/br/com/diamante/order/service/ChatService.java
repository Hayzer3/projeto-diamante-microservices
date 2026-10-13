package br.com.diamante.order.service;

import br.com.diamante.order.repository.DishRepository;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final DishRepository dishes;

    public ChatService(ChatClient.Builder builder, DishRepository dishes) {
        this.chatClient = builder.build();
        this.dishes = dishes;
    }

    public String answer(String question) {
        String menu = dishes.findAll().stream()
                .map(dish -> "%s | R$ %s | stock %d | %s".formatted(
                        dish.getName(),
                        dish.getPrice(),
                        dish.getStock(),
                        dish.getDescription()))
                .collect(Collectors.joining("\n"));

        return chatClient.prompt()
                .system(
                        "You are the restaurant's friendly attendant. "
                                + "Answer briefly in Portuguese using only this current menu:\n"
                                + menu
                                + "\nPolitely refuse questions outside the restaurant, menu, "
                                + "ingredients, prices, orders or suggestions.")
                .user(question)
                .call().content();
    }
}
