package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DirecteurDTO(
        Long id,
        @NotBlank(message = "L'email de l'utilisateur est obligatoire")
        @Email(message = "Format d'email invalide")
        String userEmail,
        @NotBlank(message = "Le grade est obligatoire")
        String grade,
        @NotBlank(message = "Le département est obligatoire")
        String departement
) {
}
