package com.galleria.backend.service;

import com.galleria.backend.dto.request.ProdutoRequestDTO;
import com.galleria.backend.dto.response.ProdutoResponseDTO;

import java.util.List;

public interface ProdutoService {
    
    ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO);
    
    ProdutoResponseDTO buscarPorId(Long id);
    
    List<ProdutoResponseDTO> listarTodos();
    
    ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO);
    
    void remover(Long id);
}
