package com.example.assignment_service.repository;

import com.example.assignment_service.domain.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    // O método findByDisciplinaId foi removido temporariamente.
}
