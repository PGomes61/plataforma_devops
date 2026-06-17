package com.example.auth_service;

import com.example.auth_service.controller.UsuarioController;
import com.example.auth_service.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UsuarioController.class)
class AuthServiceApplicationTests {

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void contextLoads() {
        // Valida com sucesso que a camada Web e as rotas carregam
        // perfeitamente sem precisar disparar conexões com o banco
    }
}
