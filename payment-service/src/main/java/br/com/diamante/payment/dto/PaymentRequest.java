package br.com.diamante.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount) {
}
