package com.galleria.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDTO(
        String descricao,

        @NotNull(message = "O ID do cliente é obrigatório")
        Long clienteId,

        @NotEmpty(message = "O pedido deve conter pelo menos um produto")
        List<Long> produtosIds
) {
}
