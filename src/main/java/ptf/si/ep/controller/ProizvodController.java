package ptf.si.ep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptf.si.ep.dto.ProizvodDto;
import ptf.si.ep.dto.ProizvodRequest;
import ptf.si.ep.service.ProizvodService;

import java.util.List;

@RestController
@RequestMapping("/api/proizvodi")
@RequiredArgsConstructor
public class ProizvodController {

    private final ProizvodService proizvodService;

    // GET /api/proizvodi?naziv=laptop&kategorijaId=1
    @GetMapping
    public List<ProizvodDto> getAll(@RequestParam(required = false) String naziv,
                                    @RequestParam(required = false) Long kategorijaId) {
        return proizvodService.findAll(naziv, kategorijaId);
    }

    @GetMapping("/{id}")
    public ProizvodDto getById(@PathVariable Long id) {
        return proizvodService.findById(id);
    }

    @GetMapping("/pred-nestankom")
    public List<ProizvodDto> getPredNestankom(@RequestParam(defaultValue = "10") int prag) {
        return proizvodService.findPredNestankom(prag);
    }

    @GetMapping("/najprodavaniji")
    public List<ProizvodDto> getNajprodavanije(@RequestParam(defaultValue = "5") int limit) {
        return proizvodService.findNajprodavanije(limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProizvodDto create(@Valid @RequestBody ProizvodRequest request) {
        return proizvodService.create(request);
    }

    @PutMapping("/{id}")
    public ProizvodDto update(@PathVariable Long id, @Valid @RequestBody ProizvodRequest request) {
        return proizvodService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        proizvodService.delete(id);
    }
}
