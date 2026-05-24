package com.galleria.backend.service.impl;

import com.galleria.backend.dto.request.ClienteRequestDTO;
import com.galleria.backend.dto.response.ClienteResponseDTO;
import com.galleria.backend.entity.Cliente;
import com.galleria.backend.exception.RegraNegocioException;
import com.galleria.backend.mapper.ClienteMapper;
import com.galleria.backend.repository.ClienteRepository;
import com.galleria.backend.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO requestDTO) {
        if (clienteRepository.existsByCpf(requestDTO.cpf())) {
            throw new RegraNegocioException("CPF já cadastrado no sistema.");
        }

        Cliente cliente = clienteMapper.toEntity(requestDTO);
        cliente = clienteRepository.save(cliente);
        
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = getClienteOuFalhar(id);
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO) {
        Cliente cliente = getClienteOuFalhar(id);

        if (clienteRepository.existsByCpfAndIdNot(requestDTO.cpf(), id)) {
            throw new RegraNegocioException("CPF já cadastrado para outro cliente.");
        }

        cliente.setNome(requestDTO.nome());
        cliente.setCpf(requestDTO.cpf());
        cliente.setTelefone(requestDTO.telefone());

        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Cliente cliente = getClienteOuFalhar(id);
        // Regra de bloqueio de exclusão se houver pedido será implementada posteriormente 
        // ou quando o domínio de pedidos for criado
        clienteRepository.delete(cliente);
    }

    private Cliente getClienteOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado."));
    }
}
