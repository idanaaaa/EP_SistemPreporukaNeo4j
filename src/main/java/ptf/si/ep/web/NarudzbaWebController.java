package ptf.si.ep.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ptf.si.ep.dto.NarudzbaDto;
import ptf.si.ep.model.StatusNarudzbe;
import ptf.si.ep.security.PrijavljeniKorisnik;
import ptf.si.ep.service.NarudzbaService;

@Controller
@RequestMapping("/narudzbe")
@RequiredArgsConstructor
public class NarudzbaWebController {

    private final NarudzbaService narudzbaService;

    // Admin vidi sve narudžbe, kupac samo svoje
    @GetMapping
    public String lista(@AuthenticationPrincipal PrijavljeniKorisnik korisnik, Model model) {
        model.addAttribute("narudzbe", narudzbaService.findAll(null, korisnik.isAdmin() ? null : korisnik.getId()));
        return "narudzbe/lista";
    }

    @GetMapping("/{id}")
    public String detalji(@PathVariable Long id, @AuthenticationPrincipal PrijavljeniKorisnik korisnik, Model model) {
        NarudzbaDto narudzba = narudzbaService.findById(id, korisnik);
        model.addAttribute("narudzba", narudzba);
        model.addAttribute("zavrsena", StatusNarudzbe.valueOf(narudzba.status()).jeZavrsen());
        model.addAttribute("statusi", StatusNarudzbe.values());
        return "narudzbe/detalji";
    }

    @PostMapping("/{id}/status")
    public String promijeniStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redirect) {
        try {
            narudzbaService.promijeniStatus(id, status);
            redirect.addFlashAttribute("poruka", "Status je promijenjen.");
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("greska", e.getReason());
        }
        return "redirect:/narudzbe/" + id;
    }
}
