package ptf.si.ep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class KupacRequest {
    @NotBlank(message = "Ime je obavezno")
    @Size(max = 80, message = "Ime može imati najviše 80 znakova")
    private String ime;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    @Size(max = 120, message = "Email može imati najviše 120 znakova")
    private String email;

    @Size(max = 120, message = "Ulica može imati najviše 120 znakova")
    private String ulica;

    @Size(max = 60, message = "Grad može imati najviše 60 znakova")
    private String grad;

    @Size(max = 10, message = "Poštanski broj može imati najviše 10 znakova")
    private String postanskiBroj;

    @Size(max = 30, message = "Telefon može imati najviše 30 znakova")
    private String telefon;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate rodjen;
}
