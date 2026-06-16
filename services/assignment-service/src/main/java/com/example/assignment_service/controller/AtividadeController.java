package com.example.assignment_service.controller;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    private final AtividadeRepository atividadeRepository;
    private final RestClient restClient;

    public AtividadeController(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;

        // Criamos o cliente diretamente usando o método estático da classe
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:8092")
            .build();
    }

    @GetMapping
    public ResponseEntity<List<Atividade>> listarTodas() {
        return ResponseEntity.ok(atividadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        Optional<Atividade> atividade = atividadeRepository.findById(id);
        return atividade
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Atividade atividade) {
        // PASSO 1: O "Telefonema" para o Academic Service
        try {
            restClient
                .get()
                .uri("/disciplinas/{id}", atividade.getDisciplinaId())
                .retrieve()
                .toBodilessEntity(); // Só queremos saber se o cabeçalho é 200 OK (Disciplina existe)
        } catch (HttpClientErrorException.NotFound e) {
            // Se a porta 8080 disser "Erro 404", nós barramos a inserção aqui na porta 8093
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Validação Falhou: A disciplina com ID " +
                    atividade.getDisciplinaId() +
                    " não existe no sistema acadêmico."
            );
        } catch (Exception e) {
            // Se a porta 8080 estiver desligada, avisamos que o serviço está indisponível
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                "Erro: O Academic Service está fora do ar ou inacessível."
            );
        }

        // PASSO 2: Se o telefonema deu certo, salvamos no nosso banco
        Atividade novaAtividade = atividadeRepository.save(atividade);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novaAtividade.getId())
            .toUri();

        return ResponseEntity.created(location).body(novaAtividade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atividade> atualizar(
        @PathVariable Long id,
        @RequestBody Atividade atividadeAtualizada
    ) {
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
