package br.ueg.desenvolvimento.web.projeto_joaopedro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    @GetMapping("/home")
    public String getHome(Model model) {
        List<Map<String, String>> alunos = new ArrayList<>();
        alunos.add(Map.of("nome", "João", "email", "joao@localhost"));
        model.addAttribute("alunos", alunos);
        return "home"; 
    }
}
