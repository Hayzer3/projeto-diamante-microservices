package br.com.diamante.payment.controller;

import br.com.diamante.payment.dto.PaymentRequest;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Value("${server.port}")
    private int port;

    @PostMapping("/payments")
    public ResponseEntity<Map<String, Object>> pay(@RequestBody PaymentRequest request) {
        if (request.amount() == null || request.amount().signum() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Amount must be positive", "instance", port));
        }

        if (ThreadLocalRandom.current().nextBoolean()) {
            System.err.printf("Payment failed on instance port %d%n", port);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "FAILED", "instance", port));
        }

        System.out.printf("Payment approved by instance on port %d%n", port);
        return ResponseEntity.ok(Map.of("status", "APPROVED", "instance", port));
    }
}
