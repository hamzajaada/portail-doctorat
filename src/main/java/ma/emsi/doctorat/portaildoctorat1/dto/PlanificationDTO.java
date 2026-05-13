package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PlanificationDTO(
        @NotNull(message = "La date de soutenance est obligatoire")
        @Future(message = "La date de soutenance doit être dans le futur")
        LocalDateTime dateSoutenance,
        
        @NotBlank(message = "Le lieu est obligatoire")
        String lieuSoutenance,
        
        String informationsComplementaires
) {}
