package ptf.si.ep.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ptf.si.ep.dto.KategorijaDto;
import ptf.si.ep.service.KategorijaService;

@Controller
@RequestMapping("/kategorije")
@RequiredArgsConstructor
public class KategorijaWebController {

    private final KategorijaService kategorijaService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("kategorije", kategorijaService.findAll());
        return "kategorije/lista";
    }

    @PostMapping
    public String dodaj(@Valid @ModelAttribute KategorijaDto kategorija, BindingResult binding,
                        RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            redirect.addFlashAttribute("greska", binding.getFieldError().getDefaultMessage());
            return "redirect:/kategorije";
        }
        try {
            KategorijaDto nova = kategorijaService.create(new KategorijaDto(null, kategorija.naziv().trim()));
            redirect.addFlashAttribute("poruka", "Kategorija '" + nova.naziv() + "' je dodana.");
        } catch (ResponseStatusException e) {
            redirect.addFlashAttribute("greska", e.getReason());
        }
        return "redirect:/kategorije";
    }

    @GetMapping("/{id}/uredi")
    public String uredi(@PathVariable Long id, Model model) {
        model.addAttribute("kategorija", kategorijaService.findById(id));
        return "kategorije/forma";
    }

    @PostMapping("/{id}")
    public String sacuvaj(@PathVariable Long id, @Valid @ModelAttribute KategorijaDto kategorija, BindingResult binding,
                          Model model, RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("greska", binding.getFieldError().getDefaultMessage());
            return "kategorije/forma";
        }
        try {
            kategorijaService.update(id, new KategorijaDto(id, kategorija.naziv().trim()));
        } catch (ResponseStatusException e) {
            model.addAttribute("greska", e.getReason());
            return "kategorije/forma";
        }
        redirect.addFlashAttribute("poruka", "Kategorija je sačuvana.");
        return "redirect:/kategorije";
    }

    @PostMapping("/{id}/obrisi")
    public String obrisi(@PathVariable Long id, RedirectAttributes redirect) {
        kategorijaService.delete(id);
        redirect.addFlashAttribute("poruka", "Kategorija je obrisana.");
        return "redirect:/kategorije";
    }
}
