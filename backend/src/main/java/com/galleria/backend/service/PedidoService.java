package com.galleria.backend.service;

import com.galleria.backend.dto.request.PedidoRequestDTO;
import com.galleria.backend.dto.response.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {
    
    PedidoResponseDTO criar(PedidoRequestDTO requestDTO);
    
    PedidoResponseDTO buscarPorId(Long id);
    
    List<PedidoResponseDTO> listarTodos();
}
