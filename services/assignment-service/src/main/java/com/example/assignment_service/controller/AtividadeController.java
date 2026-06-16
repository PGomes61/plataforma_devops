package com.example.assignment_service.controller;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/atividades") // Substantivo no plural, como manda a regra REST
public class AtividadeController {

    private final AtividadeRepository atividadeRepository;

    public AtividadeController(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    @GetMapping
    public ResponseEntity<List<Atividade>> listarTodas() {
        return ResponseEntity.ok(atividadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        Optional<Atividade> atividade = atividadeRepository.findById(id);
        return atividade.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Atividade> criar(@RequestBody Atividade atividade) {
        Atividade novaAtividade = atividadeRepository.save(atividade);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaAtividade.getId())
                .toUri();

        return ResponseEntity.created(location).body(novaAtividade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atividade> atualizar(@PathVariable Long id, @RequestBody Atividade atividadeAtualizada) {
        if (!atividadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        atividadeAtualizada.setId(id);
        return ResponseEntity.ok(atividadeRepository.save(atividadeAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!atividadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        atividadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}