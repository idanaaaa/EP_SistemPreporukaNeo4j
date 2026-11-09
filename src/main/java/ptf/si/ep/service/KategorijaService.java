package ptf.si.ep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ptf.si.ep.dto.KategorijaDto;
import ptf.si.ep.model.Kategorija;
import ptf.si.ep.repository.KategorijaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KategorijaService {

    private final KategorijaRepository kategorijaRepository;

    public List<KategorijaDto> findAll() {
        return kategorijaRepository.findAll().stream()
                .map(KategorijaDto::from)
                .toList();
    }

    public KategorijaDto findById(Long id) {
        return KategorijaDto.from(findOrThrow(id));
    }

    @Transactional
    public KategorijaDto create(KategorijaDto dto) {
        provjeriJedinstvenNaziv(dto.naziv());
        Kategorija kategorija = new Kategorija();
        kategorija.setNaziv(dto.naziv());
        return KategorijaDto.from(kategorijaRepository.save(kategorija));
    }

    @Transactional
    public KategorijaDto update(Long id, KategorijaDto dto) {
        Kategorija kategorija = findOrThrow(id);
        if (!kategorija.getNaziv().equals(dto.naziv())) {
            provjeriJedinstvenNaziv(dto.naziv());
        }
        kategorija.setNaziv(dto.naziv());
        return KategorijaDto.from(kategorija);
    }

    @Transactional
    public void delete(Long id) {
        Kategorija kategorija = findOrThrow(id);
        // Proizvod je vlasnik M:N veze, pa kategoriju prvo uklanjamo iz proizvoda (briše redove iz proizvod_kategorija)
        kategorija.getProizvods().forEach(p -> p.getKategorijas().remove(kategorija));
        kategorijaRepository.delete(kategorija);
    }

    private Kategorija findOrThrow(Long id) {
        return kategorijaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategorija " + id + " ne postoji"));
    }

    private void provjeriJedinstvenNaziv(String naziv) {
        if (kategorijaRepository.existsByNaziv(naziv)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kategorija '" + naziv + "' već postoji");
        }
    }
}
