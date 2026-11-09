package ptf.si.ep.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// Nema kupacId: kupac je uvijek prijavljeni korisnik, inače bi se moglo naručiti u tuđe ime
public record NarudzbaRequest(@NotEmpty(message = "Narudžba mora imati barem jednu stavku")
                              List<@Valid Stavka> stavke) {
    public record Stavka(@NotNull(message = "Proizvod je obavezan") Long proizvodId,
                         @NotNull(message = "Količina je obavezna")
                         @Min(value = 1, message = "Količina mora biti barem 1") Integer kolicina) {
    }
}
