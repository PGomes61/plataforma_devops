package com.example.assignment_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AtividadeServiceTest {

    @Mock
    private AtividadeRepository atividadeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AtividadeService atividadeService;

    private Atividade atividade;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
            atividadeService,
            "restTemplate",
            restTemplate
        );

        atividade = new Atividade();
        atividade.setId(1L);
        atividade.setTitulo("Estudo Dirigido");
        atividade.setDescricao("Ler capítulo 1");
        atividade.setPrazo(LocalDate.of(2026, 6, 25));
        atividade.setDisciplinaId(1L);
        atividade.setUsuarioId(1L);
    }

    @Test
    void criar_ComDisciplinaValida_DeveSalvarAtividade() {
        when(atividadeRepository.save(any(Atividade.class))).thenReturn(
            atividade
        );

        Atividade resultado = atividadeService.criar(atividade);

        assertNotNull(resultado);
        verify(atividadeRepository, times(1)).save(atividade);
    }

    @Test
    void criar_ComDisciplinaInvalida_DeveLancarIllegalArgumentException() {
        when(
            restTemplate.getForEntity(anyString(), eq(Void.class), anyLong())
        ).thenThrow(HttpClientErrorException.NotFound.class);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> atividadeService.criar(atividade)
        );

        assertTrue(exception.getMessage().contains("Validação Falhou"));
        verify(atividadeRepository, never()).save(any(Atividade.class));
    }

    @Test
    void buscarPorId_Existente_DeveRetornarAtividade() {
        when(atividadeRepository.findById(1L)).thenReturn(
            Optional.of(atividade)
        );

        Optional<Atividade> resultado = atividadeService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void buscarPorId_Inexistente_DeveRetornarVazio() {
        when(atividadeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Atividade> resultado = atividadeService.buscarPorId(1L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void deletar_AtividadeExistente_DeveExcluirComSucesso() {
        when(atividadeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(atividadeRepository).deleteById(1L);

        assertDoesNotThrow(() -> atividadeService.deletar(1L));
        verify(atividadeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_AtividadeInexistente_DeveLancarExcecao() {
        when(atividadeRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
            atividadeService.deletar(1L)
        );
        verify(atividadeRepository, never()).deleteById(anyLong());
    }
}
