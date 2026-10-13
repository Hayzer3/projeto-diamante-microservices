package br.com.diamante.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrder(
        @NotNull Long dishId,
        @Min(1) int quantity) {
}
