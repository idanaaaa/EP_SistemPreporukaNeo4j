package ptf.si.ep.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusRequest(@NotBlank(message = "Status je obavezan") String status) {
}
