package com.example.assignment_service.service;

import com.example.assignment_service.domain.Atividade;
import com.example.assignment_service.repository.AtividadeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final RestTemplate restTemplate;
    private final String academicServiceUrl;
    private final String authServiceUrl;

    public AtividadeService(
        AtividadeRepository atividadeRepository,
        @Value(
            "${ACADEMIC_SERVICE_URL:http://localhost:8092}"
        ) String academicServiceUrl,
        @Value(
            "${AUTH_SERVICE_URL:http://localhost:8081}"
        ) String authServiceUrl
    ) {
        this.atividadeRepository = atividadeRepository;
        this.academicServiceUrl = academicServiceUrl;
        this.authServiceUrl = authServiceUrl;
        this.restTemplate = new RestTemplate();
    }

    public List<Atividade> listarTodas() {
        return atividadeRepository.findAll();
    }

    public Optional<Atividade> buscarPorId(Long id) {
        return atividadeRepository.findById(id);
    }

    public Atividade criar(Atividade atividade) {
        // Validação no Academic Service
        try {
            restTemplate.getForEntity(
                academicServiceUrl + "/disciplinas/{id}",
                Void.class,
                atividade.getDisciplinaId()
            );
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

        // Validação no Auth Service
        try {
            restTemplate.getForEntity(
                authServiceUrl + "/usuarios/{id}",
                Void.class,
                atividade.getUsuarioId()
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException(
                "Validação Falhou: O usuário com ID " +
                    atividade.getUsuarioId() +
                    " não existe no sistema de autenticação."
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                "Erro: O Auth Service está fora do ar ou inacessível."
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
