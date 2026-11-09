package ptf.si.ep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ptf.si.ep.model.Kategorija;

public record KategorijaDto(Long id,
                            @NotBlank(message = "Naziv kategorije je obavezan")
                            @Size(max = 60, message = "Naziv može imati najviše 60 znakova")
                            String naziv) {
    public static KategorijaDto from(Kategorija kategorija) {
        return new KategorijaDto(kategorija.getId(), kategorija.getNaziv());
    }
}
