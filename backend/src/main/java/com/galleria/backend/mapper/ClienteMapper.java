package com.galleria.backend.mapper;

import com.galleria.backend.dto.request.ClienteRequestDTO;
import com.galleria.backend.dto.response.ClienteResponseDTO;
import com.galleria.backend.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Cliente(dto.nome(), dto.cpf(), dto.telefone());
    }

    public ClienteResponseDTO toResponseDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        return new ClienteResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone()
        );
    }
}
