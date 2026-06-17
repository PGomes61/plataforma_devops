package com.example.auth_service.controller;

import com.example.auth_service.domain.Usuario;
import com.example.auth_service.service.UsuarioService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService
            .listarTodos()
            .stream()
            .map(this::adicionarLinksHateoas)
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Usuario>> collectionModel =
            CollectionModel.of(usuarios);
        collectionModel.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    UsuarioController.class
                ).listarTodos()
            ).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> buscarPorId(
        @PathVariable Long id
    ) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(adicionarLinksHateoas(usuario));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> criar(
        @RequestBody Usuario usuario
    ) {
        Usuario novoUsuario = usuarioService.criar(usuario);
        EntityModel<Usuario> resource = adicionarLinksHateoas(novoUsuario);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novoUsuario.getId())
            .toUri();

        return ResponseEntity.created(location).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> atualizar(
        @PathVariable Long id,
        @RequestBody Usuario usuario
    ) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(adicionarLinksHateoas(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Usuario> adicionarLinksHateoas(Usuario usuario) {
        EntityModel<Usuario> resource = EntityModel.of(usuario);
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).buscarPorId(
                    usuario.getId()
                )
            ).withSelfRel()
        );
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    UsuarioController.class
                ).listarTodos()
            ).withRel("usuarios")
        );
        return resource;
    }
}
