package com.example.assignment_service.controller;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.service.AtividadeService;
import java.net.URI;
import java.util.List;
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
    public ResponseEntity<List<Atividade>> listarTodas() {
        return ResponseEntity.ok(atividadeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return atividadeService
            .buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Atividade atividade) {
        try {
            Atividade novaAtividade = atividadeService.criar(atividade);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaAtividade.getId())
                .toUri();
            return ResponseEntity.created(location).body(novaAtividade);
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
    public ResponseEntity<Atividade> atualizar(
        @PathVariable Long id,
        @RequestBody Atividade atividadeAtualizada
    ) {
        try {
            return ResponseEntity.ok(
                atividadeService.atualizar(id, atividadeAtualizada)
            );
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
}
