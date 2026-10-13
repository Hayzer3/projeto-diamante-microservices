package br.com.diamante.review.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewMessagingConfiguration {
    public static final String EXCHANGE = "delivery.exchange";
    public static final String QUEUE = "reviews.queue";
    public static final String ROUTING_KEY = "reviews.new";
    @Bean
    TopicExchange deliveryExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue reviewsQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    Binding reviewsBinding(Queue reviewsQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(reviewsQueue).to(deliveryExchange).with(ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(
            ConnectionFactory factory,
            MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
