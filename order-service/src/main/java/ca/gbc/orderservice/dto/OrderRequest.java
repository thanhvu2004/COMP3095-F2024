package ca.gbc.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(
        Long Id,
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
    public record UserDetails(String email, String firstName, String lastName) {
    }
}
