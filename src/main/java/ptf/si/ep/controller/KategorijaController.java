package ptf.si.ep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptf.si.ep.dto.KategorijaDto;
import ptf.si.ep.service.KategorijaService;

import java.util.List;

@RestController
@RequestMapping("/api/kategorije")
@RequiredArgsConstructor
public class KategorijaController {

    private final KategorijaService kategorijaService;

    @GetMapping
    public List<KategorijaDto> getAll() {
        return kategorijaService.findAll();
    }

    @GetMapping("/{id}")
    public KategorijaDto getById(@PathVariable Long id) {
        return kategorijaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KategorijaDto create(@Valid @RequestBody KategorijaDto dto) {
        return kategorijaService.create(dto);
    }

    @PutMapping("/{id}")
    public KategorijaDto update(@PathVariable Long id, @Valid @RequestBody KategorijaDto dto) {
        return kategorijaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        kategorijaService.delete(id);
    }
}
