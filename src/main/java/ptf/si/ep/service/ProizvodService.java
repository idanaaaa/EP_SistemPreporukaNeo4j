package ptf.si.ep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ptf.si.ep.dto.ProizvodDto;
import ptf.si.ep.dto.ProizvodRequest;
import ptf.si.ep.model.Kategorija;
import ptf.si.ep.model.Proizvod;
import ptf.si.ep.repository.KategorijaRepository;
import ptf.si.ep.repository.ProizvodRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProizvodService {

    private final ProizvodRepository proizvodRepository;
    private final KategorijaRepository kategorijaRepository;

    // Oba filtera su opcionalna; bez njih vraća sve proizvode po nazivu
    public List<ProizvodDto> findAll(String naziv, Long kategorijaId) {
        boolean imaNaziv = naziv != null && !naziv.isBlank();
        List<Proizvod> proizvodi;
        if (kategorijaId != null) {
            proizvodi = proizvodRepository.findByKategorijasId(kategorijaId).stream()
                    .filter(p -> !imaNaziv || p.getNaziv().toLowerCase().contains(naziv.trim().toLowerCase()))
                    .sorted(Comparator.comparing(Proizvod::getNaziv))
                    .toList();
        } else if (imaNaziv) {
            proizvodi = proizvodRepository.findByNazivContainingIgnoreCase(naziv.trim());
        } else {
            proizvodi = proizvodRepository.findAll(Sort.by("naziv"));
        }
        return proizvodi.stream().map(ProizvodDto::from).toList();
    }

    public ProizvodDto findById(Long id) {
        return ProizvodDto.from(findOrThrow(id));
    }

    // Proizvodi koji više ne postoje se jednostavno preskaču
    public List<ProizvodDto> findAllById(Collection<Long> ids) {
        return proizvodRepository.findAllById(ids).stream().map(ProizvodDto::from).toList();
    }

    public List<ProizvodDto> findPredNestankom(int prag) {
        return proizvodRepository.findPredNestankom(prag).stream().map(ProizvodDto::from).toList();
    }

    public List<ProizvodDto> findNajprodavanije(int limit) {
        return proizvodRepository.findNajprodavanije(limit).stream().map(ProizvodDto::from).toList();
    }

    @Transactional
    public ProizvodDto create(ProizvodRequest request) {
        Proizvod proizvod = new Proizvod();
        primijeni(proizvod, request);
        return ProizvodDto.from(proizvodRepository.save(proizvod));
    }

    @Transactional
    public ProizvodDto update(Long id, ProizvodRequest request) {
        Proizvod proizvod = findOrThrow(id);
        primijeni(proizvod, request);
        return ProizvodDto.from(proizvod);
    }

    @Transactional
    public void delete(Long id) {
        Proizvod proizvod = findOrThrow(id);
        // Stavke narudžbi čuvaju historiju kupovine, pa se prodani proizvod ne smije obrisati
        if (!proizvod.getStavkaNarudzbes().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Proizvod '" + proizvod.getNaziv() + "' se nalazi u narudžbama i ne može se obrisati");
        }
        proizvodRepository.delete(proizvod);
    }

    private void primijeni(Proizvod proizvod, ProizvodRequest request) {
        List<Long> kategorijaIds = request.getKategorijaIds() != null ? request.getKategorijaIds() : List.of();
        List<Kategorija> kategorije = kategorijaRepository.findAllById(kategorijaIds);
        if (kategorije.size() != new HashSet<>(kategorijaIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Neka od izabranih kategorija ne postoji");
        }

        proizvod.setNaziv(request.getNaziv().trim());
        proizvod.setOpis(request.getOpis() == null || request.getOpis().isBlank() ? null : request.getOpis().trim());
        proizvod.setCijena(request.getCijena());
        proizvod.setNaStanju(request.getNaStanju());
        proizvod.getKategorijas().clear();
        proizvod.getKategorijas().addAll(kategorije);
    }

    private Proizvod findOrThrow(Long id) {
        return proizvodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proizvod " + id + " ne postoji"));
    }
}
