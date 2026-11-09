package ptf.si.ep.dto;

import ptf.si.ep.model.StavkaNarudzbe;

import java.math.BigDecimal;

public record StavkaNarudzbeDto(Long id, Long proizvodId, String proizvodNaziv, Integer kolicina, BigDecimal cijena,
                                BigDecimal iznos) {
    public static StavkaNarudzbeDto from(StavkaNarudzbe stavka) {
        return new StavkaNarudzbeDto(stavka.getId(), stavka.getProizvod().getId(), stavka.getProizvod().getNaziv(),
                stavka.getKolicina(), stavka.getCijena(),
                stavka.getCijena().multiply(BigDecimal.valueOf(stavka.getKolicina())));
    }
}
