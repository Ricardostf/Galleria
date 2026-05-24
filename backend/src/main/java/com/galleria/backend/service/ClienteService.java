package com.galleria.backend.service;

import com.galleria.backend.dto.request.ClienteRequestDTO;
import com.galleria.backend.dto.response.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    
    ClienteResponseDTO criar(ClienteRequestDTO requestDTO);
    
    ClienteResponseDTO buscarPorId(Long id);
    
    List<ClienteResponseDTO> listarTodos();
    
    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO);
    
    void remover(Long id);
}
