package ptf.si.ep.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ptf.si.ep.dto.KorpaStavkaDto;
import ptf.si.ep.dto.NarudzbaDto;
import ptf.si.ep.dto.NarudzbaRequest;
import ptf.si.ep.dto.ProizvodDto;
import ptf.si.ep.security.PrijavljeniKorisnik;
import ptf.si.ep.service.Korpa;
import ptf.si.ep.service.KupacService;
import ptf.si.ep.service.NarudzbaService;
import ptf.si.ep.service.ProizvodService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/korpa")
@RequiredArgsConstructor
public class KorpaWebController {

    private final Korpa korpa;
    private final ProizvodService proizvodService;
    private final NarudzbaService narudzbaService;
    private final KupacService kupacService;

    @GetMapping
    public String lista(Model model) {
        dodajSadrzaj(model);
        return "korpa/lista";
    }

    @PostMapping("/dodaj")
    public String dodaj(@RequestParam Long proizvodId, @RequestParam(defaultValue = "1") int kolicina,
                        RedirectAttributes redirect) {
        ProizvodDto proizvod = proizvodService.findById(proizvodId);
        String greska = provjeriKolicinu(proizvod, kolicina, korpa.getKolicina(proizvodId));
        if (greska != null) {
            redirect.addFlashAttribute("greska", greska);
            return "redirect:/proizvodi/" + proizvodId;
        }
        korpa.postavi(proizvodId, korpa.getKolicina(proizvodId) + kolicina);
        redirect.addFlashAttribute("poruka", "Proizvod '" + proizvod.naziv() + "' je dodan u korpu.");
        return "redirect:/korpa";
    }

    @PostMapping("/{proizvodId}/kolicina")
    public String promijeniKolicinu(@PathVariable Long proizvodId, @RequestParam int kolicina,
                                    RedirectAttributes redirect) {
        String greska = provjeriKolicinu(proizvodService.findById(proizvodId), kolicina, 0);
        if (greska != null) {
            redirect.addFlashAttribute("greska", greska);
        } else {
            korpa.postavi(proizvodId, kolicina);
        }
        return "redirect:/korpa";
    }

    @PostMapping("/{proizvodId}/ukloni")
    public String ukloni(@PathVariable Long proizvodId) {
        korpa.ukloni(proizvodId);
        return "redirect:/korpa";
    }

    // Traži prijavu: Spring Security pamti ovaj GET i poslije prijave vraća kupca ovdje, a korpa ostaje u sesiji
    @GetMapping("/potvrda")
    public String potvrda(@AuthenticationPrincipal PrijavljeniKorisnik korisnik, Model model) {
        if (dodajSadrzaj(model).isEmpty()) {
            return "redirect:/korpa";
        }
        model.addAttribute("kupac", kupacService.findById(korisnik.getId()));
        return "korpa/potvrda";
    }

    @PostMapping("/potvrda")
    public String potvrdi(@AuthenticationPrincipal PrijavljeniKorisnik korisnik, Model model,
                          RedirectAttributes redirect) {
        List<KorpaStavkaDto> stavke = dodajSadrzaj(model);
        if (stavke.isEmpty()) {
            return "redirect:/korpa";
        }
        NarudzbaRequest request = new NarudzbaRequest(stavke.stream()
                .map(s -> new NarudzbaRequest.Stavka(s.proizvod().id(), s.kolicina()))
                .toList());
        try {
            NarudzbaDto narudzba = narudzbaService.create(korisnik.getId(), request);
            korpa.isprazni();
            redirect.addFlashAttribute("poruka", "Narudžba je primljena. Hvala na kupovini!");
            return "redirect:/narudzbe/" + narudzba.id();
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("greska", e.getReason());
            return "redirect:/korpa";
        }
    }

    // Ovo je samo poruka kupcu; stvarna zaštita od prodaje preko stanja je atomski UPDATE u NarudzbaService
    private String provjeriKolicinu(ProizvodDto proizvod, int kolicina, int vecUKorpi) {
        if (kolicina < 1) {
            return "Količina mora biti barem 1";
        }
        if (kolicina > proizvod.naStanju() - vecUKorpi) {
            return "Na stanju je samo " + proizvod.naStanju() + " kom";
        }
        return null;
    }

    private List<KorpaStavkaDto> dodajSadrzaj(Model model) {
        List<ProizvodDto> proizvodi = proizvodService.findAllById(korpa.getStavke().keySet());
        korpa.zadrzi(proizvodi.stream().map(ProizvodDto::id).toList());
        List<KorpaStavkaDto> stavke = proizvodi.stream()
                .map(p -> KorpaStavkaDto.of(p, korpa.getKolicina(p.id())))
                .toList();
        model.addAttribute("stavke", stavke);
        model.addAttribute("ukupno", stavke.stream().map(KorpaStavkaDto::iznos).reduce(BigDecimal.ZERO, BigDecimal::add));
        return stavke;
    }
}
