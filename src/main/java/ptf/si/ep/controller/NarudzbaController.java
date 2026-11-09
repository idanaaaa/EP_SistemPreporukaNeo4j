package ptf.si.ep.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ptf.si.ep.dto.NarudzbaDto;
import ptf.si.ep.dto.NarudzbaRequest;
import ptf.si.ep.dto.StatusRequest;
import ptf.si.ep.security.PrijavljeniKorisnik;
import ptf.si.ep.service.NarudzbaService;

import java.util.List;

@RestController
@RequestMapping("/api/narudzbe")
@RequiredArgsConstructor
public class NarudzbaController {

    private final NarudzbaService narudzbaService;

    // GET /api/narudzbe?status=NOVA ili ?kupacId=1; kupac uvijek dobija samo svoje narudžbe
    @GetMapping
    public List<NarudzbaDto> getAll(@RequestParam(required = false) String status,
                                    @RequestParam(required = false) Long kupacId,
                                    @AuthenticationPrincipal PrijavljeniKorisnik korisnik) {
        return narudzbaService.findAll(status, korisnik.isAdmin() ? kupacId : korisnik.getId());
    }

    @GetMapping("/{id}")
    public NarudzbaDto getById(@PathVariable Long id, @AuthenticationPrincipal PrijavljeniKorisnik korisnik) {
        return narudzbaService.findById(id, korisnik);
    }

    // { "stavke": [ { "proizvodId": 2, "kolicina": 3 } ] }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NarudzbaDto create(@Valid @RequestBody NarudzbaRequest request,
                              @AuthenticationPrincipal PrijavljeniKorisnik korisnik) {
        return narudzbaService.create(korisnik.getId(), request);
    }

    // { "status": "POSLANA" }
    @PatchMapping("/{id}/status")
    public NarudzbaDto promijeniStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return narudzbaService.promijeniStatus(id, request.status());
    }
}
