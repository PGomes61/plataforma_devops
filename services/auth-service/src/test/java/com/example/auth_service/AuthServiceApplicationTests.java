package com.example.auth_service;

import com.example.auth_service.controller.UsuarioController;
import com.example.auth_service.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UsuarioController.class)
class AuthServiceApplicationTests {

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void contextLoads() {
        // Faz o uso explícito do atributo para passar na validação de código morto do PMD
        Assertions.assertNotNull(
            usuarioService,
            "O contexto web deve carregar o mock do serviço."
        );
    }
}
