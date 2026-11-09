package ptf.si.ep.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ptf.si.ep.dto.RegistracijaRequest;
import ptf.si.ep.service.KupacService;

import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class PrijavaWebController {

    // BCrypt koristi najviše 72 bajta lozinke, a slova poput č i ž zauzimaju po dva
    private static final int MAX_BAJTOVA_LOZINKE = 72;

    private final KupacService kupacService;

    // POST /prijava i POST /odjava obrađuje Spring Security (SecurityConfig), ovdje je samo stranica s formom
    @GetMapping("/prijava")
    public String prijava() {
        return "prijava";
    }

    @GetMapping("/registracija")
    public String registracija(Model model) {
        model.addAttribute("kupac", new RegistracijaRequest());
        return "registracija";
    }

    @PostMapping("/registracija")
    public String registruj(@Valid @ModelAttribute("kupac") RegistracijaRequest request, BindingResult binding,
                            RedirectAttributes redirect) {
        if (request.getLozinka() != null
                && request.getLozinka().getBytes(StandardCharsets.UTF_8).length > MAX_BAJTOVA_LOZINKE) {
            binding.rejectValue("lozinka", "preduga", "Lozinka je preduga");
        }
        if (binding.hasErrors()) {
            return "registracija";
        }
        try {
            kupacService.registruj(request);
        } catch (ResponseStatusException e) {
            binding.rejectValue("email", "zauzet", e.getReason());
            return "registracija";
        }
        redirect.addFlashAttribute("poruka", "Račun je kreiran. Prijavite se.");
        return "redirect:/prijava";
    }
}
