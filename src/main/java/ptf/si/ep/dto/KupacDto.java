package ptf.si.ep.dto;

import ptf.si.ep.model.Kupac;
import ptf.si.ep.model.Profil;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Profil (1:1) je spljošten u kupca - klijent ne mora znati da su to dvije tabele
public record KupacDto(Long id, String ime, String email, String ulica, String grad, String postanskiBroj,
                       LocalDateTime kreirano, String telefon, LocalDate rodjen) {
    public static KupacDto from(Kupac kupac) {
        Profil profil = kupac.getProfil();
        return new KupacDto(kupac.getId(), kupac.getIme(), kupac.getEmail(), kupac.getUlica(), kupac.getGrad(),
                kupac.getPostanskiBroj(), kupac.getKreirano(),
                profil != null ? profil.getTelefon() : null,
                profil != null ? profil.getRodjen() : null);
    }
}
