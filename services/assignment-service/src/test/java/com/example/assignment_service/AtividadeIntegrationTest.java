package com.example.assignment_service;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AtividadeIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private AtividadeRepository repository;

    @Test
    void deveSalvarERecuperarAtividadeNoBancoPostgresReal() {
        // Arrange
        Atividade novaAtividade = new Atividade();
        novaAtividade.setTitulo("Implementação de Algoritmo");
        novaAtividade.setDescricao("Implementar um conversor para a disciplina de microcontroladores.");
        novaAtividade.setPrazo(LocalDate.now().plusDays(7));
        novaAtividade.setDisciplinaId(10L);
        novaAtividade.setUsuarioId(5L);

        // Act
        Atividade salva = repository.save(novaAtividade);
        Atividade recuperada = repository.findById(salva.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(recuperada);
        Assertions.assertEquals("Implementação de Algoritmo", recuperada.getTitulo());
        Assertions.assertTrue(postgres.isRunning());
    }
}
