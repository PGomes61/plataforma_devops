package com.example.academic_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.academic_service.domain.Disciplina;
import com.example.academic_service.repository.DisciplinaRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisciplinaServiceTest {

    @Mock
    private DisciplinaRepository disciplinaRepository;

    @InjectMocks
    private DisciplinaService disciplinaService;

    private Disciplina disciplina;

    @BeforeEach
    void setUp() {
        disciplina = new Disciplina();
        disciplina.setId(1L);
        disciplina.setNome("Engenharia de Software");
        disciplina.setCodigo("ENG-SOFT");
        disciplina.setCargaHoraria(60);
    }

    @Test
    void criar_DeveSalvarDisciplina() {
        when(disciplinaRepository.save(any(Disciplina.class))).thenReturn(
            disciplina
        );

        Disciplina resultado = disciplinaService.criar(disciplina);

        assertNotNull(resultado);
        assertEquals("ENG-SOFT", resultado.getCodigo());
        verify(disciplinaRepository, times(1)).save(disciplina);
    }

    @Test
    void buscarPorId_Existente_DeveRetornarDisciplina() {
        when(disciplinaRepository.findById(1L)).thenReturn(
            Optional.of(disciplina)
        );

        Optional<Disciplina> resultado = disciplinaService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void deletar_DisciplinaExistente_DeveExcluirComSucesso() {
        when(disciplinaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(disciplinaRepository).deleteById(1L);

        assertDoesNotThrow(() -> disciplinaService.deletar(1L));
        verify(disciplinaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletar_DisciplinaInexistente_DeveLancarExcecao() {
        when(disciplinaRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
            disciplinaService.deletar(1L)
        );
        verify(disciplinaRepository, never()).deleteById(anyLong());
    }
}
