package br.com.diamante.order.config;

import br.com.diamante.order.model.Dish;
import br.com.diamante.order.repository.DishRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderDataLoader {
    @Bean
    CommandLineRunner seedDishes(DishRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        dish(
                                "Diamond Burger",
                                "Brioche, beef, cheddar and house sauce",
                                "39.90",
                                10),
                        dish(
                                "Veggie Garden Bowl",
                                "Roasted vegetables, quinoa and tahini",
                                "34.90",
                                20),
                        dish(
                                "Chicken Parmigiana",
                                "Breaded chicken, tomato sauce and rice",
                                "42.00",
                                15),
                        dish("Margherita Pizza", "Tomato, mozzarella and basil", "45.00", 12),
                        dish("Chocolate Brownie", "Warm chocolate brownie", "16.50", 30)));
            }
        };
    }

    private Dish dish(String name, String description, String price, int stock) {
        return new Dish(name, description, new BigDecimal(price), stock);
    }
}
