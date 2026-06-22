package com.example.auth_service;

import com.example.auth_service.domain.Usuario;
import com.example.auth_service.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// Sobe a aplicação inteira em uma porta aleatória
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Avisa o JUnit para gerenciar o ciclo de vida dos containers
@Testcontainers
class UsuarioIntegrationTest {

    // A Mágica: Sobe a MESMA imagem do PostgreSQL que você usa no docker-stack.yml
    // A anotação @ServiceConnection injeta a URL, usuário e senha automaticamente no Spring!
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        "postgres:15-alpine"
    );

    @Autowired
    private UsuarioRepository repository;

    @Test
    void deveSalvarERecuperarUsuarioNoBancoPostgresReal() {
        // Arrange: Prepara os dados
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Paulo Integration");
        novoUsuario.setEmail("paulo.teste@email.com");
        novoUsuario.setSenha("senha123");
        novoUsuario.setTipo("ADMIN");

        // Act: Salva no container do PostgreSQL real
        Usuario salvo = repository.save(novoUsuario);
        Usuario recuperado = repository.findById(salvo.getId()).orElse(null);

        // Assert: Valida se o banco guardou e devolveu corretamente
        Assertions.assertNotNull(recuperado);
        Assertions.assertEquals("Paulo Integration", recuperado.getNome());
        Assertions.assertEquals("paulo.teste@email.com", recuperado.getEmail());

        // Bônus: Valida que o container está realmente rodando durante o teste
        Assertions.assertTrue(postgres.isRunning());
    }
}
