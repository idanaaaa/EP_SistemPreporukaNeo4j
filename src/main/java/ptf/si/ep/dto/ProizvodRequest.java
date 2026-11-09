package ptf.si.ep.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Klasa (ne record) jer je koristi i Thymeleaf forma - th:field traži gettere i settere
@Data
public class ProizvodRequest {
    @NotBlank(message = "Naziv proizvoda je obavezan")
    @Size(max = 120, message = "Naziv može imati najviše 120 znakova")
    private String naziv;

    private String opis;

    @NotNull(message = "Cijena je obavezna")
    @DecimalMin(value = "0.00", message = "Cijena mora biti 0 ili veća")
    private BigDecimal cijena;

    @NotNull(message = "Stanje je obavezno")
    @Min(value = 0, message = "Stanje ne može biti negativno")
    private Integer naStanju = 0;

    private List<Long> kategorijaIds = new ArrayList<>();

    public static ProizvodRequest from(ProizvodDto dto) {
        ProizvodRequest request = new ProizvodRequest();
        request.setNaziv(dto.naziv());
        request.setOpis(dto.opis());
        request.setCijena(dto.cijena());
        request.setNaStanju(dto.naStanju());
        request.setKategorijaIds(new ArrayList<>(dto.kategorije().stream().map(KategorijaDto::id).toList()));
        return request;
    }
}
