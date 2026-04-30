package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DoctorantDTO(
        Long id,
        @NotBlank(message = "L'email de l'utilisateur est obligatoire")
        @Email(message = "Format d'email invalide")
        String userEmail,
        @NotBlank(message = "Le CNE est obligatoire")
        String cne,
        @NotNull(message = "L'année de thèse est obligatoire")
        Integer anneeThese,
        @NotNull(message = "La date de première inscription est obligatoire")
        LocalDate datePremiereInscription
) {
}
