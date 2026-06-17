package com.example.academic_service.controller;

import com.example.academic_service.domain.Disciplina;
import com.example.academic_service.service.DisciplinaService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public ResponseEntity<
        CollectionModel<EntityModel<Disciplina>>
    > listarTodas() {
        List<EntityModel<Disciplina>> disciplinas = disciplinaService
            .listarTodas()
            .stream()
            .map(this::adicionarLinksHateoas)
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Disciplina>> collectionModel =
            CollectionModel.of(disciplinas);
        collectionModel.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    DisciplinaController.class
                ).listarTodas()
            ).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Disciplina>> buscarPorId(
        @PathVariable Long id
    ) {
        return disciplinaService
            .buscarPorId(id)
            .map(this::adicionarLinksHateoas) // Aplica o HATEOAS se existir
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build()); // Mantém o seu 404 original
    }

    @PostMapping
    public ResponseEntity<EntityModel<Disciplina>> criar(
        @RequestBody Disciplina disciplina
    ) {
        Disciplina novaDisciplina = disciplinaService.criar(disciplina);
        EntityModel<Disciplina> resource = adicionarLinksHateoas(
            novaDisciplina
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novaDisciplina.getId())
            .toUri();

        return ResponseEntity.created(location).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Disciplina>> atualizar(
        @PathVariable Long id,
        @RequestBody Disciplina disciplinaAtualizada
    ) {
        try {
            Disciplina atualizada = disciplinaService.atualizar(
                id,
                disciplinaAtualizada
            );
            return ResponseEntity.ok(adicionarLinksHateoas(atualizada));
        } catch (IllegalArgumentException e) {
            // Mantém a sua captura de erro original
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            disciplinaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Mantém a sua captura de erro original
            return ResponseEntity.notFound().build();
        }
    }

    // Método utilitário privado para construir a hipermídia
    private EntityModel<Disciplina> adicionarLinksHateoas(
        Disciplina disciplina
    ) {
        EntityModel<Disciplina> resource = EntityModel.of(disciplina);
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    DisciplinaController.class
                ).buscarPorId(disciplina.getId())
            ).withSelfRel()
        );
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    DisciplinaController.class
                ).listarTodas()
            ).withRel("disciplinas")
        );
        return resource;
    }
}
