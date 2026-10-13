package br.com.diamante.order.service;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClient {
    private final RestTemplate restTemplate;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(maxRetries = 4, delay = 150, multiplier = 2.0, jitter = 100, maxDelay = 1500)
    public Map<?, ?> charge(BigDecimal amount) {
        return restTemplate.postForObject(
                "http://PAYMENT-SERVICE/payments",
                new HttpEntity<>(Map.of("amount", amount)),
                Map.class);
    }
}
