package ptf.si.ep.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

// Svaka sesija dobija svoju korpu; u bazu ide tek potvrđena narudžba.
// Čuva samo id-eve i količine, pa se cijena i stanje uvijek čitaju iz baze
@Component
@SessionScope
public class Korpa implements Serializable {

    private final Map<Long, Integer> stavke = new LinkedHashMap<>();

    public Map<Long, Integer> getStavke() {
        return Collections.unmodifiableMap(stavke);
    }

    public int getKolicina(Long proizvodId) {
        return stavke.getOrDefault(proizvodId, 0);
    }

    public int getBrojKomada() {
        return stavke.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void postavi(Long proizvodId, int kolicina) {
        stavke.put(proizvodId, kolicina);
    }

    public void ukloni(Long proizvodId) {
        stavke.remove(proizvodId);
    }

    // Izbacuje proizvode koji su u međuvremenu obrisani
    public void zadrzi(Collection<Long> proizvodIds) {
        stavke.keySet().retainAll(proizvodIds);
    }

    public void isprazni() {
        stavke.clear();
    }
}
