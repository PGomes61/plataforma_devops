package com.example.assignment_service.service;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final RestClient restClient;

    // Injetando a URL dinamicamente via parâmetro do construtor
    public AtividadeService(
        AtividadeRepository atividadeRepository,
        @Value(
            "${ACADEMIC_SERVICE_URL:http://localhost:8092}"
        ) String academicServiceUrl
    ) {
        this.atividadeRepository = atividadeRepository;
        this.restClient = RestClient.builder()
            .baseUrl(academicServiceUrl) // Substitui o "http://localhost:8092" fixo
            .build();
    }

    public List<Atividade> listarTodas() {
        return atividadeRepository.findAll();
    }

    public Optional<Atividade> buscarPorId(Long id) {
        return atividadeRepository.findById(id);
    }

    public Atividade criar(Atividade atividade) {
        try {
            restClient
                .get()
                .uri("/disciplinas/{id}", atividade.getDisciplinaId())
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException(
                "Validação Falhou: A disciplina com ID " +
                    atividade.getDisciplinaId() +
                    " não existe no sistema acadêmico."
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                "Erro: O Academic Service está fora do ar ou inacessível."
            );
        }

        return atividadeRepository.save(atividade);
    }

    public Atividade atualizar(Long id, Atividade atividadeAtualizada) {
        if (!atividadeRepository.existsById(id)) {
            throw new IllegalArgumentException("Atividade não encontrada.");
        }
        atividadeAtualizada.setId(id);
        return atividadeRepository.save(atividadeAtualizada);
    }

    public void deletar(Long id) {
        if (!atividadeRepository.existsById(id)) {
            throw new IllegalArgumentException("Atividade não encontrada.");
        }
        atividadeRepository.deleteById(id);
    }
}
