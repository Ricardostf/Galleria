package com.galleria.backend.service.impl;

import com.galleria.backend.dto.request.ProdutoRequestDTO;
import com.galleria.backend.dto.response.ProdutoResponseDTO;
import com.galleria.backend.entity.Produto;
import com.galleria.backend.exception.RegraNegocioException;
import com.galleria.backend.mapper.ProdutoMapper;
import com.galleria.backend.repository.ItemPedidoRepository;
import com.galleria.backend.repository.ProdutoRepository;
import com.galleria.backend.service.ProdutoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO) {
        Produto produto = produtoMapper.toEntity(requestDTO);
        produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = getProdutoOuFalhar(id);
        return produtoMapper.toResponseDTO(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {
        Produto produto = getProdutoOuFalhar(id);

        produto.setDescricao(requestDTO.descricao());
        produto.setValor(requestDTO.valor());

        produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(produto);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Produto produto = getProdutoOuFalhar(id);
        
        if (itemPedidoRepository.existsByProdutoId(id)) {
            throw new RegraNegocioException("Não é possível remover produto que já foi incluído em pedidos.");
        }
        
        produtoRepository.delete(produto);
    }

    private Produto getProdutoOuFalhar(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Produto não encontrado."));
    }
}
