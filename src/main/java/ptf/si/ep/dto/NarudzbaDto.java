package ptf.si.ep.dto;

import ptf.si.ep.model.Narudzba;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record NarudzbaDto(Long id, Long kupacId, String kupacIme, String status, BigDecimal ukupno,
                          LocalDateTime kreirano, List<StavkaNarudzbeDto> stavke) {
    public static NarudzbaDto from(Narudzba narudzba) {
        return new NarudzbaDto(narudzba.getId(), narudzba.getKupac().getId(), narudzba.getKupac().getIme(),
                narudzba.getStatus(), narudzba.getUkupno(), narudzba.getKreirano(),
                narudzba.getStavkaNarudzbes().stream()
                        .map(StavkaNarudzbeDto::from)
                        .sorted(Comparator.comparing(StavkaNarudzbeDto::id))
                        .toList());
    }
}
