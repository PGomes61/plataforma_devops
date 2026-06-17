package com.example.assignment_service.controller;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.service.AtividadeService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    @GetMapping
    public ResponseEntity<
        CollectionModel<EntityModel<Atividade>>
    > listarTodas() {
        List<EntityModel<Atividade>> atividades = atividadeService
            .listarTodas()
            .stream()
            .map(this::adicionarLinksHateoas)
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Atividade>> collectionModel =
            CollectionModel.of(atividades);
        collectionModel.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    AtividadeController.class
                ).listarTodas()
            ).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Atividade>> buscarPorId(
        @PathVariable Long id
    ) {
        return atividadeService
            .buscarPorId(id)
            .map(this::adicionarLinksHateoas)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Atividade atividade) {
        try {
            Atividade novaAtividade = atividadeService.criar(atividade);
            EntityModel<Atividade> resource = adicionarLinksHateoas(
                novaAtividade
            );

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaAtividade.getId())
                .toUri();

            return ResponseEntity.created(location).body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                e.getMessage()
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                e.getMessage()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Atividade>> atualizar(
        @PathVariable Long id,
        @RequestBody Atividade atividadeAtualizada
    ) {
        try {
            Atividade atualizada = atividadeService.atualizar(
                id,
                atividadeAtualizada
            );
            return ResponseEntity.ok(adicionarLinksHateoas(atualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            atividadeService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método utilitário privado para construir a hipermídia
    private EntityModel<Atividade> adicionarLinksHateoas(Atividade atividade) {
        EntityModel<Atividade> resource = EntityModel.of(atividade);
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    AtividadeController.class
                ).buscarPorId(atividade.getId())
            ).withSelfRel()
        );
        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    AtividadeController.class
                ).listarTodas()
            ).withRel("atividades")
        );
        return resource;
    }
}
