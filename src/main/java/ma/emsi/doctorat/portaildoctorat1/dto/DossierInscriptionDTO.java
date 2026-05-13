package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DossierInscriptionDTO(
        Long id,
        @NotNull(message = "L'ID du doctorant est obligatoire")
        Long doctorantId,
        @NotNull(message = "L'ID du directeur est obligatoire")
        Long directeurId,
        @NotNull(message = "L'ID de la campagne est obligatoire")
        Long campagneId,
        @NotBlank(message = "Le sujet de thèse est obligatoire")
        String sujetThese,
        String doctorantNom,
        String doctorantPrenom,
        String doctorantEmail,
        String statut,
        LocalDateTime dateDepot
) {
}
