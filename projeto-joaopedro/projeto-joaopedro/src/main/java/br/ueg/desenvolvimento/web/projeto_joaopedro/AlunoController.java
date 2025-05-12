package br.ueg.desenvolvimento.web.projeto_joaopedro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ueg.desenvolvimento.web.projeto_joaopedro.model.Aluno;
import br.ueg.desenvolvimento.web.projeto_joaopedro.repository.AlunoRepository;

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    // Lista todos os alunos
    @GetMapping("/alunos")
    public String getHome(Model model) {
        List<Aluno> alunosBd = alunoRepository.findAll();
        model.addAttribute("alunos", alunosBd);
        model.addAttribute("mensagem", "Todos os alunos cadastrados");
        return "alunos.html";
    }

    // Página inicial
    @GetMapping("/")
    public String getHome() {
        return "home.html";
    }

    // Página de criação de aluno
    @GetMapping("/alunos/create")
    public String getCreate(Model model) {
        model.addAttribute("aluno", new Aluno()); // Adiciona um objeto vazio de Aluno
        return "alunos-create.html";
    }

    // Salva um novo aluno
    @PostMapping("/alunos/create")
    public String postCreate(@ModelAttribute Aluno aluno, Model model) {
        // Verifica se já existe um aluno com a mesma matrícula ou e-mail
        boolean existsByMatricula = alunoRepository.existsByMatricula(aluno.getMatricula());
        boolean existsByEmail = alunoRepository.existsByEmail(aluno.getEmail());

        if (existsByMatricula || existsByEmail) {
            model.addAttribute("erro", "Já existe um aluno com a mesma matrícula ou e-mail.");
            model.addAttribute("aluno", aluno);
            return "alunos-create.html"; // Retorna à página de criação com a mensagem de erro
        }

        alunoRepository.save(aluno); // Salva o aluno no banco de dados
        return "redirect:/alunos";
    }

    // Página de atualização de aluno
    @GetMapping("/alunos/update/{id}")
    public String getUpdate(@PathVariable Integer id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno != null) {
            model.addAttribute("aluno", aluno);
            return "alunos-update.html";
        }
        return "redirect:/alunos";
    }

    // Atualiza um aluno existente
    @PostMapping("/alunos/update")
    public String postUpdate(@ModelAttribute Aluno aluno, Model model) {
        // Verifica se já existe outro aluno com a mesma matrícula ou e-mail
        boolean existsByMatricula = alunoRepository.existsByMatriculaAndIdNot(aluno.getMatricula(), aluno.getId());
        boolean existsByEmail = alunoRepository.existsByEmailAndIdNot(aluno.getEmail(), aluno.getId());

        if (existsByMatricula || existsByEmail) {
            model.addAttribute("erro", "Já existe outro aluno com a mesma matrícula ou e-mail.");
            model.addAttribute("aluno", aluno);
            return "alunos-update.html"; // Retorna à página de edição com a mensagem de erro
        }

        alunoRepository.save(aluno); // Atualiza o aluno no banco de dados
        return "redirect:/alunos";
    }

    // Página de confirmação de exclusão
    @GetMapping("/alunos/delete/confirm/{id}")
    public String getDeleteConfirm(@PathVariable Integer id, Model model) {
        Aluno alunoDb = alunoRepository.findById(id).orElse(null);
        if (alunoDb != null) {
            model.addAttribute("aluno", alunoDb);
            return "alunos-delete-confirm.html"; // Página de confirmação
        }
        return "redirect:/alunos";
    }

    // Exclui um aluno
    @PostMapping("/alunos/delete")
    public String postDelete(@RequestParam Integer id) {
        alunoRepository.deleteById(id); // Exclui o aluno pelo ID
        return "redirect:/alunos"; // Redireciona para a lista de alunos
    }

    // Busca alunos por nome, matrícula ou e-mail
    @GetMapping("/alunos/busca")
    public String getBusca(@RequestParam(required = false) String nome,
                           @RequestParam(required = false) String matricula,
                           @RequestParam(required = false) String email,
                           Model model) {
        List<Aluno> alunos;

        if (nome != null && !nome.isEmpty()) {
            alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
        } else if (matricula != null && !matricula.isEmpty()) {
            alunos = alunoRepository.findByMatriculaContainingIgnoreCase(matricula);
        } else if (email != null && !email.isEmpty()) {
            alunos = alunoRepository.findByEmailContainingIgnoreCase(email);
        } else {
            alunos = alunoRepository.findAll(); // Retorna todos os alunos se nenhum filtro for fornecido
        }

        model.addAttribute("alunos", alunos);
        return "alunos.html";
    }
}