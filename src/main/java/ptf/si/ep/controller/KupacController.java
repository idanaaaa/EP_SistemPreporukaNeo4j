package ptf.si.ep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptf.si.ep.dto.KupacDto;
import ptf.si.ep.dto.KupacRequest;
import ptf.si.ep.dto.NarudzbaDto;
import ptf.si.ep.service.KupacService;
import ptf.si.ep.service.NarudzbaService;

import java.util.List;

@RestController
@RequestMapping("/api/kupci")
@RequiredArgsConstructor
public class KupacController {

    private final KupacService kupacService;
    private final NarudzbaService narudzbaService;

    // GET /api/kupci?grad=Sarajevo
    @GetMapping
    public List<KupacDto> getAll(@RequestParam(required = false) String grad) {
        return kupacService.findAll(grad);
    }

    @GetMapping("/{id}")
    public KupacDto getById(@PathVariable Long id) {
        return kupacService.findById(id);
    }

    @GetMapping("/{id}/narudzbe")
    public List<NarudzbaDto> getNarudzbe(@PathVariable Long id) {
        kupacService.findById(id); // 404 ako kupac ne postoji, umjesto prazne liste
        return narudzbaService.findAll(null, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KupacDto create(@Valid @RequestBody KupacRequest request) {
        return kupacService.create(request);
    }

    @PutMapping("/{id}")
    public KupacDto update(@PathVariable Long id, @Valid @RequestBody KupacRequest request) {
        return kupacService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        kupacService.delete(id);
    }
}
