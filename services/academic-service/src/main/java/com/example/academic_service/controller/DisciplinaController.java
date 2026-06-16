package com.example.academic_service.controller;

import com.example.academic_service.domain.Disciplina;
import com.example.academic_service.repository.DisciplinaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaController(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    // GET /disciplinas -> Retorna a lista (Status 200 OK)
    @GetMapping
    public ResponseEntity<List<Disciplina>> listarTodas() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        return ResponseEntity.ok(disciplinas);
    }

    // GET /disciplinas/{id} -> Retorna um recurso específico
    @GetMapping("/{id}")
    public ResponseEntity<Disciplina> buscarPorId(@PathVariable Long id) {
        Optional<Disciplina> disciplina = disciplinaRepository.findById(id);

        // Se não achar, devolve 404 Not Found em vez de quebrar a aplicação
        return disciplina.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /disciplinas -> Cria um novo recurso
    @PostMapping
    public ResponseEntity<Disciplina> criar(@RequestBody Disciplina disciplina) {
        Disciplina novaDisciplina = disciplinaRepository.save(disciplina);

        // Regra de Ouro do POST: Status 201 Created + Cabeçalho "Location" com a URL do novo recurso
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaDisciplina.getId())
                .toUri();

        return ResponseEntity.created(location).body(novaDisciplina);
    }

    // PUT /disciplinas/{id} -> Atualiza o recurso inteiro (Idempotente)
    @PutMapping("/{id}")
    public ResponseEntity<Disciplina> atualizar(@PathVariable Long id, @RequestBody Disciplina disciplinaAtualizada) {
        if (!disciplinaRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        disciplinaAtualizada.setId(id); // Garante que não vai criar um novo, mas sim sobrescrever
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplinaAtualizada);

        return ResponseEntity.ok(disciplinaSalva); // 200 OK
    }

    // DELETE /disciplinas/{id} -> Remove o recurso (Idempotente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!disciplinaRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        disciplinaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content (Sucesso, sem corpo na resposta)
    }
}