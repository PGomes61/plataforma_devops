package com.example.auth_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.auth_service.domain.Usuario;
import com.example.auth_service.exception.UsuarioNaoEncontradoException;
import com.example.auth_service.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Admin");
        usuario.setEmail("admin@teste.com");
        usuario.setSenha("senha123");
        usuario.setTipo("ADMIN");
    }

    @Test
    void buscarPorId_DeveRetornarUsuario_QuandoIdExistir() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
            service.buscarPorId(99L)
        );
        verify(repository, times(1)).findById(99L);
    }
}
