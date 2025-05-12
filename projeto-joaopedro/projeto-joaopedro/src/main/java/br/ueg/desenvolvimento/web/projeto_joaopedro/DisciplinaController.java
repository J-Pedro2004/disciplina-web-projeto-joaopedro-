package br.ueg.desenvolvimento.web.projeto_joaopedro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.ueg.desenvolvimento.web.projeto_joaopedro.model.Disciplina;
import br.ueg.desenvolvimento.web.projeto_joaopedro.repository.DisciplinaRepository;

@Controller
public class DisciplinaController {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @GetMapping("/disciplinas")
    public String listarDisciplinas(Model model) {
        model.addAttribute("disciplinas", disciplinaRepository.findAll());
        model.addAttribute("disciplina", new Disciplina());
        return "disciplinas.html";
    }

    @PostMapping("/disciplinas")
    public String salvarDisciplina(@ModelAttribute Disciplina disciplina) {
        disciplinaRepository.save(disciplina);
        return "redirect:/disciplinas";
    }
}