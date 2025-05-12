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
import br.ueg.desenvolvimento.web.projeto_joaopedro.model.TelefoneAluno;
import br.ueg.desenvolvimento.web.projeto_joaopedro.repository.AlunoRepository;
import br.ueg.desenvolvimento.web.projeto_joaopedro.repository.DisciplinaRepository;
import br.ueg.desenvolvimento.web.projeto_joaopedro.repository.TelefoneAlunoRepository;

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private TelefoneAlunoRepository telefoneAlunoRepository;

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
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("todasDisciplinas", disciplinaRepository.findAll());
        return "alunos-create.html";
    }

    // Salva um novo aluno
    @PostMapping("/alunos/create")
    public String postCreate(@ModelAttribute Aluno aluno, Model model) {
        boolean existsByMatricula = alunoRepository.existsByMatricula(aluno.getMatricula());
        boolean existsByEmail = alunoRepository.existsByEmail(aluno.getEmail());

        if (existsByMatricula || existsByEmail) {
            model.addAttribute("erro", "Já existe um aluno com a mesma matrícula ou e-mail.");
            model.addAttribute("aluno", aluno);
            model.addAttribute("todasDisciplinas", disciplinaRepository.findAll());
            return "alunos-create.html";
        }

        alunoRepository.save(aluno);
        return "redirect:/alunos";
    }

    // Página de atualização de aluno
    @GetMapping("/alunos/update/{id}")
    public String getUpdate(@PathVariable Integer id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno != null) {
            model.addAttribute("aluno", aluno);
            model.addAttribute("todasDisciplinas", disciplinaRepository.findAll());
            return "alunos-update.html";
        }
        return "redirect:/alunos";
    }

    // Atualiza um aluno existente
    @PostMapping("/alunos/update")
    public String postUpdate(@ModelAttribute Aluno aluno, Model model) {
        boolean existsByMatricula = alunoRepository.existsByMatriculaAndIdNot(aluno.getMatricula(), aluno.getId());
        boolean existsByEmail = alunoRepository.existsByEmailAndIdNot(aluno.getEmail(), aluno.getId());

        if (existsByMatricula || existsByEmail) {
            model.addAttribute("erro", "Já existe outro aluno com a mesma matrícula ou e-mail.");
            model.addAttribute("aluno", aluno);
            model.addAttribute("todasDisciplinas", disciplinaRepository.findAll());
            return "alunos-update.html";
        }

        alunoRepository.save(aluno);
        return "redirect:/alunos";
    }

    // Tela de confirmação para remover disciplina do aluno
    @GetMapping("/alunos/{alunoId}/disciplinas/{disciplinaId}/remover/confirm")
    public String confirmarRemoverDisciplina(@PathVariable Integer alunoId,
                                             @PathVariable Integer disciplinaId,
                                             Model model) {
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        var disciplina = disciplinaRepository.findById(disciplinaId).orElse(null);
        if (aluno == null || disciplina == null) {
            return "redirect:/alunos/update/" + alunoId;
        }
        model.addAttribute("aluno", aluno);
        model.addAttribute("disciplina", disciplina);
        return "aluno-disciplina-delete-confirm.html";
    }

    // Remove disciplina do aluno (após confirmação)
    @PostMapping("/alunos/{alunoId}/disciplinas/{disciplinaId}/remover")
    public String removerDisciplinaDoAluno(@PathVariable Integer alunoId, @PathVariable Integer disciplinaId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        if (aluno != null) {
            aluno.getDisciplinas().removeIf(d -> d.getId().equals(disciplinaId));
            alunoRepository.save(aluno);
        }
        return "redirect:/alunos/update/" + alunoId;
    }

    // Página de confirmação de exclusão
    @GetMapping("/alunos/delete/confirm/{id}")
    public String getDeleteConfirm(@PathVariable Integer id, Model model) {
        Aluno alunoDb = alunoRepository.findById(id).orElse(null);
        if (alunoDb != null) {
            model.addAttribute("aluno", alunoDb);
            return "alunos-delete-confirm.html";
        }
        return "redirect:/alunos";
    }

    // Exclui um aluno
    @PostMapping("/alunos/delete")
    public String postDelete(@RequestParam Integer id) {
        alunoRepository.deleteById(id);
        return "redirect:/alunos";
    }

    // Busca alunos por nome, matrícula, e-mail ou disciplina
    @GetMapping("/alunos/busca")
    public String getBusca(@RequestParam(required = false) String nome,
                           @RequestParam(required = false) String matricula,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String disciplina,
                           Model model) {
        List<Aluno> alunos;

        if (disciplina != null && !disciplina.isEmpty()) {
            alunos = alunoRepository.findDistinctByDisciplinasNomeContainingIgnoreCase(disciplina);
        } else if (nome != null && !nome.isEmpty()) {
            alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
        } else if (matricula != null && !matricula.isEmpty()) {
            alunos = alunoRepository.findByMatriculaContainingIgnoreCase(matricula);
        } else if (email != null && !email.isEmpty()) {
            alunos = alunoRepository.findByEmailContainingIgnoreCase(email);
        } else {
            alunos = alunoRepository.findAll();
        }

        model.addAttribute("alunos", alunos);
        return "alunos.html";
    }

    // --- Métodos para telefones do aluno ---

    // Lista os telefones do aluno
    @GetMapping("/alunos/{id}/telefones")
    public String listarTelefones(@PathVariable Integer id, Model model) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno == null) {
            return "redirect:/alunos";
        }
        List<TelefoneAluno> telefones = telefoneAlunoRepository.findByAlunoId(id);
        model.addAttribute("aluno", aluno);
        model.addAttribute("telefones", telefones);
        return "alunos-telefone.html";
    }

    // Adiciona um telefone ao aluno
    @PostMapping("/alunos/{id}/telefones/add")
    public String adicionarTelefone(@PathVariable Integer id,
                                    @RequestParam String numero,
                                    @RequestParam(required = false) Boolean whatsapp) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno != null) {
            TelefoneAluno telefone = new TelefoneAluno();
            telefone.setNumero(numero);
            telefone.setWhatsapp(whatsapp != null ? whatsapp : false);
            telefone.setAluno(aluno);
            telefoneAlunoRepository.save(telefone);
        }
        return "redirect:/alunos/" + id + "/telefones";
    }

    // Remove um telefone do aluno
    @PostMapping("/alunos/{alunoId}/telefones/{telefoneId}/delete")
    public String removerTelefone(@PathVariable Integer alunoId, @PathVariable Integer telefoneId) {
        telefoneAlunoRepository.deleteById(telefoneId);
        return "redirect:/alunos/" + alunoId + "/telefones";
    }
}