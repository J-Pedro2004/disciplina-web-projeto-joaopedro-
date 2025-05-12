package br.ueg.desenvolvimento.web.projeto_joaopedro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.desenvolvimento.web.projeto_joaopedro.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
    List<Aluno> findByMatriculaContainingIgnoreCase(String matricula);
    List<Aluno> findByEmailContainingIgnoreCase(String email);
    List<Aluno> findDistinctByDisciplinasNomeContainingIgnoreCase(String nome);
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    boolean existsByMatriculaAndIdNot(String matricula, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
  
}

