package ptf.si.ep.dto;

import java.math.BigDecimal;

public record KorpaStavkaDto(ProizvodDto proizvod, int kolicina, BigDecimal iznos) {
    public static KorpaStavkaDto of(ProizvodDto proizvod, int kolicina) {
        return new KorpaStavkaDto(proizvod, kolicina, proizvod.cijena().multiply(BigDecimal.valueOf(kolicina)));
    }
}
