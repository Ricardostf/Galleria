package com.galleria.backend.dto.response;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long id,
        Long produtoId,
        String produtoDescricao,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {
}
