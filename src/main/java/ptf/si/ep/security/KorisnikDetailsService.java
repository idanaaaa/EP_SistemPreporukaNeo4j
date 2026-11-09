package ptf.si.ep.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ptf.si.ep.repository.KupacRepository;

@Service
@RequiredArgsConstructor
public class KorisnikDetailsService implements UserDetailsService {

    private final KupacRepository kupacRepository;

    // "Username" je email; kupac bez lozinke (napravljen preko API-ja) se ne može prijaviti
    @Override
    public UserDetails loadUserByUsername(String email) {
        return kupacRepository.findByEmail(email.trim().toLowerCase())
                .filter(kupac -> kupac.getLozinka() != null)
                .map(PrijavljeniKorisnik::new)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik '" + email + "' ne postoji"));
    }
}
