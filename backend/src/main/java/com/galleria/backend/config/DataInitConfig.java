package com.galleria.backend.config;

import com.galleria.backend.entity.Cliente;
import com.galleria.backend.entity.ItemPedido;
import com.galleria.backend.entity.Pedido;
import com.galleria.backend.entity.Produto;
import com.galleria.backend.entity.Usuario;
import com.galleria.backend.repository.ClienteRepository;
import com.galleria.backend.repository.PedidoRepository;
import com.galleria.backend.repository.ProdutoRepository;
import com.galleria.backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, 
                                      ClienteRepository clienteRepository,
                                      ProdutoRepository produtoRepository,
                                      PedidoRepository pedidoRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Seeder: Usuário
            if (usuarioRepository.findByLogin("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin"));
                admin.setAtivo(true);
                usuarioRepository.save(admin);
                System.out.println("Seeder: Usuário padrão admin/admin criado com sucesso!");
            }

            // Seeder: Clientes
            if (clienteRepository.count() == 0) {
                Cliente c1 = new Cliente("João da Silva", "11122233344", "joao@email.com");
                Cliente c2 = new Cliente("Maria Oliveira", "55566677788", "maria@email.com");
                clienteRepository.saveAll(List.of(c1, c2));
                System.out.println("Seeder: Clientes criados com sucesso!");
            }

            // Seeder: Produtos
            if (produtoRepository.count() == 0) {
                Produto p1 = new Produto("Notebook Dell Inspiron", new BigDecimal("4500.00"));
                Produto p2 = new Produto("Mouse Sem Fio Logitech", new BigDecimal("120.50"));
                Produto p3 = new Produto("Teclado Mecânico", new BigDecimal("350.00"));
                Produto p4 = new Produto("Monitor LG 29 Ultrawide", new BigDecimal("1300.00"));
                produtoRepository.saveAll(List.of(p1, p2, p3, p4));
                System.out.println("Seeder: Produtos criados com sucesso!");
            }

            // Seeder: Pedidos (somente se não houver pedidos e existirem clientes/produtos)
            if (pedidoRepository.count() == 0 && clienteRepository.count() > 0 && produtoRepository.count() > 0) {
                Cliente cliente = clienteRepository.findAll().get(0);
                Produto p1 = produtoRepository.findAll().get(0);
                Produto p2 = produtoRepository.findAll().get(1);

                Pedido pedido = new Pedido("Pedido inicial de teste", cliente);
                
                ItemPedido item1 = new ItemPedido(p1, 1);
                ItemPedido item2 = new ItemPedido(p2, 2);

                pedido.adicionarItem(item1);
                pedido.adicionarItem(item2);

                pedidoRepository.save(pedido);
                System.out.println("Seeder: Pedido inicial criado com sucesso!");
            }
        };
    }
}
