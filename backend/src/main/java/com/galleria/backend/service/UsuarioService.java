package com.galleria.backend.service;

import com.galleria.backend.dto.request.UsuarioRequestDTO;
import com.galleria.backend.dto.request.UsuarioUpdateDTO;
import com.galleria.backend.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    
    UsuarioResponseDTO criar(UsuarioRequestDTO requestDTO);
    
    UsuarioResponseDTO buscarPorId(Long id);
    
    List<UsuarioResponseDTO> listarTodos();
    
    UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO updateDTO);
    
    void remover(Long id);
}
