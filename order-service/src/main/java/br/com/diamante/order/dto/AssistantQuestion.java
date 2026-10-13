package br.com.diamante.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssistantQuestion(@NotNull @NotBlank String question) {
}
