package ptf.si.ep.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ptf.si.ep.service.ProizvodService;

@Controller
@RequiredArgsConstructor
public class PocetnaWebController {

    private final ProizvodService proizvodService;

    @GetMapping("/")
    public String pocetna(Model model) {
        model.addAttribute("predNestankom", proizvodService.findPredNestankom(10));
        model.addAttribute("najprodavaniji", proizvodService.findNajprodavanije(5));
        return "index";
    }
}
