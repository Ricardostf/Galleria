package com.galleria.backend.config;

import com.galleria.backend.entity.Usuario;
import com.galleria.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByLogin("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin"));
                admin.setAtivo(true);
                usuarioRepository.save(admin);
                System.out.println("Usuário padrão admin/admin criado com sucesso!");
            }
        };
    }
}
