package ptf.si.ep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ptf.si.ep.dto.KupacDto;
import ptf.si.ep.dto.KupacRequest;
import ptf.si.ep.dto.RegistracijaRequest;
import ptf.si.ep.model.Kupac;
import ptf.si.ep.model.Profil;
import ptf.si.ep.repository.KupacRepository;
import ptf.si.ep.repository.ProfilRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KupacService {

    private final KupacRepository kupacRepository;
    private final ProfilRepository profilRepository;
    private final PasswordEncoder passwordEncoder;

    public List<KupacDto> findAll(String grad) {
        List<Kupac> kupci = grad != null && !grad.isBlank()
                ? kupacRepository.findByGradIgnoreCase(grad.trim())
                : kupacRepository.findAll(Sort.by("ime"));
        return kupci.stream().map(KupacDto::from).toList();
    }

    public KupacDto findById(Long id) {
        return KupacDto.from(findOrThrow(id));
    }

    @Transactional
    public KupacDto create(KupacRequest request) {
        provjeriJedinstvenEmail(request.getEmail());
        Kupac kupac = new Kupac();
        primijeni(kupac, request);
        kupacRepository.save(kupac);
        sacuvajProfil(kupac, request);
        return KupacDto.from(kupac);
    }

    @Transactional
    public KupacDto registruj(RegistracijaRequest request) {
        provjeriJedinstvenEmail(request.getEmail());
        Kupac kupac = new Kupac();
        kupac.setIme(request.getIme().trim());
        kupac.setEmail(request.getEmail().trim().toLowerCase());
        kupac.setLozinka(passwordEncoder.encode(request.getLozinka()));
        kupac.setUlica(request.getUlica().trim());
        kupac.setGrad(request.getGrad().trim());
        kupac.setPostanskiBroj(request.getPostanskiBroj().trim());
        return KupacDto.from(kupacRepository.save(kupac));
    }

    @Transactional
    public KupacDto update(Long id, KupacRequest request) {
        Kupac kupac = findOrThrow(id);
        if (!kupac.getEmail().equalsIgnoreCase(request.getEmail().trim())) {
            provjeriJedinstvenEmail(request.getEmail());
        }
        primijeni(kupac, request);
        sacuvajProfil(kupac, request);
        return KupacDto.from(kupac);
    }

    @Transactional
    public void delete(Long id) {
        Kupac kupac = findOrThrow(id);
        if (!kupac.getNarudzbas().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Kupac '" + kupac.getIme() + "' ima narudžbe i ne može se obrisati");
        }
        // Profil je vlasnik 1:1 veze (kolona kupac_id), pa se briše prvi
        if (kupac.getProfil() != null) {
            profilRepository.delete(kupac.getProfil());
        }
        kupacRepository.delete(kupac);
    }

    private void primijeni(Kupac kupac, KupacRequest request) {
        kupac.setIme(request.getIme().trim());
        kupac.setEmail(request.getEmail().trim().toLowerCase());
        kupac.setUlica(request.getUlica());
        kupac.setGrad(request.getGrad());
        kupac.setPostanskiBroj(request.getPostanskiBroj());
    }

    // Profil se pravi tek kad postoji neki podatak za njega
    private void sacuvajProfil(Kupac kupac, KupacRequest request) {
        boolean imaPodatke = (request.getTelefon() != null && !request.getTelefon().isBlank())
                || request.getRodjen() != null;
        Profil profil = kupac.getProfil();
        if (profil == null && !imaPodatke) {
            return;
        }
        if (profil == null) {
            profil = new Profil();
            profil.setKupac(kupac);
            kupac.setProfil(profil);
        }
        profil.setTelefon(request.getTelefon());
        profil.setRodjen(request.getRodjen());
        profilRepository.save(profil);
    }

    private void provjeriJedinstvenEmail(String email) {
        if (kupacRepository.existsByEmail(email.trim().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kupac sa emailom '" + email + "' već postoji");
        }
    }

    private Kupac findOrThrow(Long id) {
        return kupacRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kupac " + id + " ne postoji"));
    }
}
