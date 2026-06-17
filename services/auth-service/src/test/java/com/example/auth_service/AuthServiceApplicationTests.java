package com.example.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// Desativa a tentativa de conectar ao banco físico durante o carregamento deste teste
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
class AuthServiceApplicationTests {

    @Test
    void contextLoads() {}
}
