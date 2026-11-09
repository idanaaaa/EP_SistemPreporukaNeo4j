package ptf.si.ep.dto;

import ptf.si.ep.model.Proizvod;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public record ProizvodDto(Long id, String naziv, String opis, BigDecimal cijena, Integer naStanju,
                          List<KategorijaDto> kategorije) {
    public static ProizvodDto from(Proizvod proizvod) {
        return new ProizvodDto(proizvod.getId(), proizvod.getNaziv(), proizvod.getOpis(), proizvod.getCijena(),
                proizvod.getNaStanju(),
                proizvod.getKategorijas().stream()
                        .map(KategorijaDto::from)
                        .sorted(Comparator.comparing(KategorijaDto::naziv))
                        .toList());
    }
}
