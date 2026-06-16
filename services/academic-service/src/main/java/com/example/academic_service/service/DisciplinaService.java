package com.example.academic_service.service;

import com.example.academic_service.domain.Disciplina;
import com.example.academic_service.repository.DisciplinaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    public DisciplinaService(DisciplinaRepository disciplinaRepository) {
        this.disciplinaRepository = disciplinaRepository;
    }

    public List<Disciplina> listarTodas() {
        return disciplinaRepository.findAll();
    }

    public Optional<Disciplina> buscarPorId(Long id) {
        return disciplinaRepository.findById(id);
    }

    public Disciplina criar(Disciplina disciplina) {
        return disciplinaRepository.save(disciplina);
    }

    public Disciplina atualizar(Long id, Disciplina disciplinaAtualizada) {
        if (!disciplinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Disciplina não encontrada.");
        }
        disciplinaAtualizada.setId(id);
        return disciplinaRepository.save(disciplinaAtualizada);
    }

    public void deletar(Long id) {
        if (!disciplinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Disciplina não encontrada.");
        }
        disciplinaRepository.deleteById(id);
    }
}
