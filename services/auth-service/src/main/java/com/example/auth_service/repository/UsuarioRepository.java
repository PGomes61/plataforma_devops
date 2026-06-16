package com.example.auth_service.repository;

import com.example.auth_service.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring lê o nome da função e cria o SQL sozinho:
    // SELECT * FROM tb_usuarios WHERE email = ?
    Optional<Usuario> findByEmail(String email);
}
