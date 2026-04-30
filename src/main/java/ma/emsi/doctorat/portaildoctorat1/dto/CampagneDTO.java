package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record CampagneDTO(
        Long id,
        @NotBlank(message = "L'année universitaire est obligatoire")
        String anneeUniv,
        @NotNull(message = "La date d'ouverture est obligatoire")
        LocalDate dateOuverture,
        @NotNull(message = "La date de fermeture est obligatoire")
        @FutureOrPresent(message = "La date de fermeture doit être dans le futur")
        LocalDate dateFermeture,
        Boolean active
) {
}
