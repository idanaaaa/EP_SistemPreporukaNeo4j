package ptf.si.ep.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ptf.si.ep.dto.ProizvodDto;
import ptf.si.ep.dto.ProizvodRequest;
import ptf.si.ep.service.KategorijaService;
import ptf.si.ep.service.ProizvodService;

@Controller
@RequestMapping("/proizvodi")
@RequiredArgsConstructor
public class ProizvodWebController {

    private final ProizvodService proizvodService;
    private final KategorijaService kategorijaService;

    @GetMapping
    public String lista(@RequestParam(required = false) String naziv,
                        @RequestParam(required = false) Long kategorijaId,
                        Model model) {
        model.addAttribute("proizvodi", proizvodService.findAll(naziv, kategorijaId));
        model.addAttribute("kategorije", kategorijaService.findAll());
        model.addAttribute("naziv", naziv);
        model.addAttribute("kategorijaId", kategorijaId);
        return "proizvodi/lista";
    }

    @GetMapping("/{id}")
    public String detalji(@PathVariable Long id, Model model) {
        model.addAttribute("proizvod", proizvodService.findById(id));
        return "proizvodi/detalji";
    }

    @GetMapping("/novi")
    public String novi(Model model) {
        return forma(model, null, new ProizvodRequest(), null);
    }

    @PostMapping
    public String dodaj(@Valid @ModelAttribute("proizvod") ProizvodRequest request, BindingResult binding,
                        Model model, RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            return forma(model, null, request, null);
        }
        try {
            ProizvodDto novi = proizvodService.create(request);
            redirect.addFlashAttribute("poruka", "Proizvod '" + novi.naziv() + "' je dodan.");
            return "redirect:/proizvodi/" + novi.id();
        } catch (ResponseStatusException e) {
            return forma(model, null, request, e.getReason());
        }
    }

    @GetMapping("/{id}/uredi")
    public String uredi(@PathVariable Long id, Model model) {
        return forma(model, id, ProizvodRequest.from(proizvodService.findById(id)), null);
    }

    @PostMapping("/{id}")
    public String sacuvaj(@PathVariable Long id, @Valid @ModelAttribute("proizvod") ProizvodRequest request,
                          BindingResult binding, Model model, RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            return forma(model, id, request, null);
        }
        try {
            proizvodService.update(id, request);
        } catch (ResponseStatusException e) {
            return forma(model, id, request, e.getReason());
        }
        redirect.addFlashAttribute("poruka", "Proizvod je sačuvan.");
        return "redirect:/proizvodi/" + id;
    }

    @PostMapping("/{id}/obrisi")
    public String obrisi(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            proizvodService.delete(id);
            redirect.addFlashAttribute("poruka", "Proizvod je obrisan.");
            return "redirect:/proizvodi";
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("greska", e.getReason());
            return "redirect:/proizvodi/" + id;
        }
    }

    // Ista forma služi za dodavanje (id == null) i uređivanje
    private String forma(Model model, Long id, ProizvodRequest request, String greska) {
        model.addAttribute("id", id);
        model.addAttribute("proizvod", request);
        model.addAttribute("kategorije", kategorijaService.findAll());
        model.addAttribute("greska", greska);
        return "proizvodi/forma";
    }
}
