package com.galleria.backend.repository;

import com.galleria.backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
    
    boolean existsByCpfAndIdNot(String cpf, Long id);
}
