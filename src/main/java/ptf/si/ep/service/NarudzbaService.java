package ptf.si.ep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ptf.si.ep.dto.NarudzbaDto;
import ptf.si.ep.dto.NarudzbaRequest;
import ptf.si.ep.model.*;
import ptf.si.ep.repository.KupacRepository;
import ptf.si.ep.repository.NarudzbaRepository;
import ptf.si.ep.repository.ProizvodRepository;
import ptf.si.ep.repository.StavkaNarudzbeRepository;
import ptf.si.ep.security.PrijavljeniKorisnik;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NarudzbaService {

    private final NarudzbaRepository narudzbaRepository;
    private final StavkaNarudzbeRepository stavkaRepository;
    private final KupacRepository kupacRepository;
    private final ProizvodRepository proizvodRepository;

    public List<NarudzbaDto> findAll(String status, Long kupacId) {
        List<Narudzba> narudzbe;
        if (kupacId != null) {
            narudzbe = narudzbaRepository.findByKupacIdOrderByKreiranoDesc(kupacId);
        } else if (status != null && !status.isBlank()) {
            narudzbe = narudzbaRepository.findByStatus(parseStatus(status).name());
        } else {
            narudzbe = narudzbaRepository.findAll(Sort.by(Sort.Direction.DESC, "kreirano"));
        }
        return narudzbe.stream().map(NarudzbaDto::from).toList();
    }

    // Tuđa narudžba izgleda kao da ne postoji (404), da se ne otkriva koje narudžbe postoje
    public NarudzbaDto findById(Long id, PrijavljeniKorisnik korisnik) {
        Narudzba narudzba = findOrThrow(id);
        if (!korisnik.isAdmin() && !narudzba.getKupac().getId().equals(korisnik.getId())) {
            throw nePostoji(id);
        }
        return NarudzbaDto.from(narudzba);
    }

    // Cijela narudžba je jedna transakcija: ako ijedne stavke nema na stanju, ništa se ne snima
    @Transactional
    public NarudzbaDto create(Long kupacId, NarudzbaRequest request) {
        Kupac kupac = kupacRepository.findById(kupacId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Kupac " + kupacId + " ne postoji"));

        Narudzba narudzba = new Narudzba();
        narudzba.setKupac(kupac);
        narudzba.setStatus(StatusNarudzbe.NOVA.name());
        narudzba.setUkupno(BigDecimal.ZERO);
        narudzbaRepository.save(narudzba);

        BigDecimal ukupno = BigDecimal.ZERO;
        for (NarudzbaRequest.Stavka zahtjev : request.stavke()) {
            Proizvod proizvod = proizvodRepository.findById(zahtjev.proizvodId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Proizvod " + zahtjev.proizvodId() + " ne postoji"));
            // Atomski UPDATE ... WHERE na_stanju >= kolicina; 0 promijenjenih redova = nema dovoljno
            if (proizvodRepository.smanjiStanje(proizvod.getId(), zahtjev.kolicina()) == 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Proizvoda '" + proizvod.getNaziv() + "' nema dovoljno na stanju");
            }

            StavkaNarudzbe stavka = new StavkaNarudzbe();
            stavka.setNarudzba(narudzba);
            stavka.setProizvod(proizvod);
            stavka.setKolicina(zahtjev.kolicina());
            stavka.setCijena(proizvod.getCijena());
            stavkaRepository.save(stavka);
            narudzba.getStavkaNarudzbes().add(stavka);

            ukupno = ukupno.add(proizvod.getCijena().multiply(BigDecimal.valueOf(zahtjev.kolicina())));
        }
        narudzba.setUkupno(ukupno);
        return NarudzbaDto.from(narudzba);
    }

    @Transactional
    public NarudzbaDto promijeniStatus(Long id, String noviStatus) {
        Narudzba narudzba = findOrThrow(id);
        StatusNarudzbe trenutni = parseStatus(narudzba.getStatus());
        StatusNarudzbe novi = parseStatus(noviStatus);
        if (trenutni.jeZavrsen()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Narudžba " + id + " je već " + trenutni + " i status se ne može mijenjati");
        }
        // Otkazana narudžba vraća robu na stanje
        if (novi == StatusNarudzbe.OTKAZANA) {
            narudzba.getStavkaNarudzbes().forEach(s ->
                    s.getProizvod().setNaStanju(s.getProizvod().getNaStanju() + s.getKolicina()));
        }
        narudzba.setStatus(novi.name());
        return NarudzbaDto.from(narudzba);
    }

    private StatusNarudzbe parseStatus(String status) {
        try {
            return StatusNarudzbe.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nepoznat status '" + status + "'");
        }
    }

    private Narudzba findOrThrow(Long id) {
        return narudzbaRepository.findById(id).orElseThrow(() -> nePostoji(id));
    }

    private ResponseStatusException nePostoji(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Narudžba " + id + " ne postoji");
    }
}
