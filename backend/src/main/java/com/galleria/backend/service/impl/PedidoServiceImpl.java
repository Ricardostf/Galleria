package com.galleria.backend.service.impl;

import com.galleria.backend.dto.request.PedidoRequestDTO;
import com.galleria.backend.dto.response.PedidoResponseDTO;
import com.galleria.backend.entity.Cliente;
import com.galleria.backend.entity.ItemPedido;
import com.galleria.backend.entity.Pedido;
import com.galleria.backend.entity.Produto;
import com.galleria.backend.exception.RegraNegocioException;
import com.galleria.backend.mapper.PedidoMapper;
import com.galleria.backend.repository.ClienteRepository;
import com.galleria.backend.repository.PedidoRepository;
import com.galleria.backend.repository.ProdutoRepository;
import com.galleria.backend.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ProdutoRepository produtoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.clienteId())
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + requestDTO.clienteId()));

        Pedido pedido = new Pedido(requestDTO.descricao(), cliente);

        // Agrupa os IDs dos produtos por quantidade
        Map<Long, Long> produtosQtd = requestDTO.produtosIds().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (Map.Entry<Long, Long> entry : produtosQtd.entrySet()) {
            Produto produto = produtoRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RegraNegocioException("Produto não encontrado com ID: " + entry.getKey()));
            
            ItemPedido item = new ItemPedido(produto, entry.getValue().intValue());
            pedido.adicionarItem(item);
        }

        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado."));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
