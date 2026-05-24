package com.galleria.backend.mapper;

import com.galleria.backend.dto.request.ProdutoRequestDTO;
import com.galleria.backend.dto.response.ProdutoResponseDTO;
import com.galleria.backend.entity.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Produto(dto.descricao(), dto.valor());
    }

    public ProdutoResponseDTO toResponseDTO(Produto entity) {
        if (entity == null) {
            return null;
        }
        return new ProdutoResponseDTO(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor()
        );
    }
}
