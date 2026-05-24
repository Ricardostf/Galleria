package com.galleria.backend.service.impl;

import com.galleria.backend.dto.request.UsuarioRequestDTO;
import com.galleria.backend.dto.request.UsuarioUpdateDTO;
import com.galleria.backend.dto.response.UsuarioResponseDTO;
import com.galleria.backend.entity.Usuario;
import com.galleria.backend.exception.RegraNegocioException;
import com.galleria.backend.mapper.UsuarioMapper;
import com.galleria.backend.repository.UsuarioRepository;
import com.galleria.backend.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO requestDTO) {
        if (usuarioRepository.existsByLogin(requestDTO.login())) {
            throw new RegraNegocioException("Login já cadastrado no sistema.");
        }

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setSenha(passwordEncoder.encode(requestDTO.senha()));
        
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = getUsuarioOuFalhar(id);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .filter(Usuario::isAtivo) // retornar apenas ativos na listagem, se desejado
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO updateDTO) {
        Usuario usuario = getUsuarioOuFalhar(id);

        if (usuarioRepository.existsByLoginAndIdNot(updateDTO.login(), id)) {
            throw new RegraNegocioException("Login já em uso por outro usuário.");
        }

        usuario.setNome(updateDTO.nome());
        usuario.setLogin(updateDTO.login());
        
        if (updateDTO.senha() != null && !updateDTO.senha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(updateDTO.senha()));
        }

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Usuario usuario = getUsuarioOuFalhar(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario getUsuarioOuFalhar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado."));
    }
}
