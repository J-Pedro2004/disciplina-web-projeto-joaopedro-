package br.ueg.desenvolvimento.web.projeto_joaopedro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.desenvolvimento.web.projeto_joaopedro.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
}