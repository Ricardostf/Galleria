package com.galleria.backend.mapper;

import com.galleria.backend.dto.request.UsuarioRequestDTO;
import com.galleria.backend.dto.response.UsuarioResponseDTO;
import com.galleria.backend.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Usuario(dto.nome(), dto.login(), dto.senha());
    }

    public UsuarioResponseDTO toResponseDTO(Usuario entity) {
        if (entity == null) {
            return null;
        }
        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getLogin(),
                entity.isAtivo()
        );
    }
}
