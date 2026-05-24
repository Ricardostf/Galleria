package com.galleria.backend.mapper;

import com.galleria.backend.dto.response.ItemPedidoResponseDTO;
import com.galleria.backend.dto.response.PedidoResponseDTO;
import com.galleria.backend.entity.ItemPedido;
import com.galleria.backend.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    private final ClienteMapper clienteMapper;

    public PedidoMapper(ClienteMapper clienteMapper) {
        this.clienteMapper = clienteMapper;
    }

    public PedidoResponseDTO toResponseDTO(Pedido entity) {
        if (entity == null) {
            return null;
        }
        
        List<ItemPedidoResponseDTO> itensDTO = entity.getItens().stream()
                .map(this::toItemResponseDTO)
                .collect(Collectors.toList());

        return new PedidoResponseDTO(
                entity.getId(),
                entity.getNumero(),
                entity.getDataEmissao(),
                entity.getDescricao(),
                clienteMapper.toResponseDTO(entity.getCliente()),
                itensDTO,
                entity.getTotal()
        );
    }

    private ItemPedidoResponseDTO toItemResponseDTO(ItemPedido item) {
        if (item == null) {
            return null;
        }
        return new ItemPedidoResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getQuantidade(),
                item.getValorUnitario(),
                item.getSubtotal()
        );
    }
}
