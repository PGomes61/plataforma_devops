package com.example.academic_service.repository;

import com.example.academic_service.domain.Disciplina;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    // Busca automática pelo código da disciplina (ex: "ENG-SOFT")
    Optional<Disciplina> findByCodigo(String codigo);
}
