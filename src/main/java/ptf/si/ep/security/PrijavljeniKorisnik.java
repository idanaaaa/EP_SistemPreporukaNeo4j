package ptf.si.ep.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ptf.si.ep.model.Kupac;
import ptf.si.ep.model.Uloga;

import java.util.List;

// Čuva se u sesiji, pa ne drži entitet nego samo ono što kontroleri trebaju
@Getter
public class PrijavljeniKorisnik extends User {

    private final Long id;
    private final String ime;
    private final boolean admin;

    public PrijavljeniKorisnik(Kupac kupac) {
        super(kupac.getEmail(), kupac.getLozinka(), List.of(new SimpleGrantedAuthority("ROLE_" + kupac.getUloga())));
        this.id = kupac.getId();
        this.ime = kupac.getIme();
        this.admin = Uloga.ADMIN.name().equals(kupac.getUloga());
    }
}
