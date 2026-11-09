package ptf.si.ep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class RegistracijaRequest {
    @NotBlank(message = "Ime je obavezno")
    @Size(max = 80, message = "Ime može imati najviše 80 znakova")
    private String ime;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email nije ispravan")
    @Size(max = 120, message = "Email može imati najviše 120 znakova")
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 8, message = "Lozinka mora imati barem 8 znakova")
    @ToString.Exclude
    private String lozinka;

    @NotBlank(message = "Ulica je obavezna")
    @Size(max = 120, message = "Ulica može imati najviše 120 znakova")
    private String ulica;

    @NotBlank(message = "Grad je obavezan")
    @Size(max = 60, message = "Grad može imati najviše 60 znakova")
    private String grad;

    @NotBlank(message = "Poštanski broj je obavezan")
    @Size(max = 10, message = "Poštanski broj može imati najviše 10 znakova")
    private String postanskiBroj;
}
