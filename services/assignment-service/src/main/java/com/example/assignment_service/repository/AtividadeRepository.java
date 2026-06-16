package com.example.assignment_service.repository;

import com.example.assignment_service.domain.Atividade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    // Retorna todas as atividades vinculadas a uma disciplina específica
    List<Atividade> findByDisciplinaId(Long disciplinaId);
}
