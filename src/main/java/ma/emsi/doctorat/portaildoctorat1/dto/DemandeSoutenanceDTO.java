package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DemandeSoutenanceDTO(
        Long id,
        @NotNull(message = "L'ID du doctorant est obligatoire")
        Long doctorantId,
        int nbPublications,
        int nbConferences,
        int heuresFormation,
        String statut,
        LocalDateTime dateSoutenance,
        String lieuSoutenance
) {
}
