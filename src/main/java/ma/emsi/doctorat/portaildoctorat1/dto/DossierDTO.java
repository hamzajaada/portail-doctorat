package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DossierDTO(
        @NotBlank(message = "Le sujet de thèse est obligatoire")
        String sujetThese,
        @NotNull(message = "L'ID du directeur est obligatoire")
        Long directeurId,
        @NotNull(message = "L'ID de la campagne est obligatoire")
        Long campagneId
) {
}
