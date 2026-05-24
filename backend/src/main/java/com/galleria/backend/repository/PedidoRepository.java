package com.galleria.backend.repository;

import com.galleria.backend.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    boolean existsByClienteId(Long clienteId);
    
    // Para bloqueio de exclusão de produto: precisamos verificar ItemPedido, 
    // mas também podemos fazer em ProdutoRepository ou ProdutoService.
    // Vamos criar o ItemPedidoRepository para isso ou query no PedidoRepository
}
