package br.com.diamante.order.controller;

import br.com.diamante.order.exception.OutOfStockException;
import br.com.diamante.order.exception.PaymentFailedException;
import br.com.diamante.order.exception.ResourceNotFoundException;
import br.com.diamante.order.exception.TooManyRequestsException;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(RuntimeException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OutOfStockException.class)
    ResponseEntity<Map<String, String>> conflict(RuntimeException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    ResponseEntity<Map<String, String>> gateway(RuntimeException ex) {
        return error(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(TooManyRequestsException.class)
    ResponseEntity<Map<String, String>> limited() {
        return error(HttpStatus.TOO_MANY_REQUESTS, "Order rate limit exceeded");
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    ResponseEntity<Map<String, String>> badRequest(Exception ex) {
        return error(HttpStatus.BAD_REQUEST, "Invalid request");
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
