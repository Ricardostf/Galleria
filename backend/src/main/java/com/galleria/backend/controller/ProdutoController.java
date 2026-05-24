package com.galleria.backend.controller;

import com.galleria.backend.dto.request.ProdutoRequestDTO;
import com.galleria.backend.dto.response.ProdutoResponseDTO;
import com.galleria.backend.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody @Valid ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO response = produtoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO requestDTO) {
        return ResponseEntity.ok(produtoService.atualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
