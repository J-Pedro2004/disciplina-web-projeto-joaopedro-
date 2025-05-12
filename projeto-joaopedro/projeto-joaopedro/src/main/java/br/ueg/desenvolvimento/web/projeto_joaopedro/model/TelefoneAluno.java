package br.ueg.desenvolvimento.web.projeto_joaopedro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TelefoneAluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String numero;
    private Boolean whatsapp;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    // Getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Boolean getWhatsapp() { return whatsapp; }
    public void setWhatsapp(Boolean whatsapp) { this.whatsapp = whatsapp; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
}