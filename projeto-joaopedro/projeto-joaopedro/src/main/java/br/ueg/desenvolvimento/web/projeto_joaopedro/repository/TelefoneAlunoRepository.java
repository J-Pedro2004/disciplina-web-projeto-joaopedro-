package br.ueg.desenvolvimento.web.projeto_joaopedro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.desenvolvimento.web.projeto_joaopedro.model.TelefoneAluno;

public interface TelefoneAlunoRepository extends JpaRepository<TelefoneAluno, Integer> {
    List<TelefoneAluno> findByAlunoId(Integer alunoId);
}