package com.example.auth_service.service;

import com.example.auth_service.domain.Usuario;
import com.example.auth_service.exception.UsuarioNaoEncontradoException;
import com.example.auth_service.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario criar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dadosAtualizados) {
        Usuario usuarioExistente = buscarPorId(id);
        usuarioExistente.setNome(dadosAtualizados.getNome());
        usuarioExistente.setEmail(dadosAtualizados.getEmail());
        usuarioExistente.setTipo(dadosAtualizados.getTipo());
        return repository.save(usuarioExistente);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }
}
