package com.example.academic_service;

import com.example.academic_service.domain.Disciplina;
import com.example.academic_service.repository.DisciplinaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DisciplinaIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private DisciplinaRepository repository;

    @Test
    void deveSalvarERecuperarDisciplinaNoBancoPostgresReal() {
        // Arrange
        Disciplina novaDisciplina = new Disciplina();
        novaDisciplina.setNome("Engenharia de Software");
        novaDisciplina.setCodigo("ENG123");
        novaDisciplina.setCargaHoraria(60); // <-- Propriedade obrigatória adicionada!

        // Act
        Disciplina salva = repository.save(novaDisciplina);
        Disciplina recuperada = repository.findById(salva.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(recuperada);
        Assertions.assertEquals("Engenharia de Software", recuperada.getNome());
        Assertions.assertEquals(60, recuperada.getCargaHoraria());
        Assertions.assertTrue(postgres.isRunning());
    }
}
