package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JuryDTO(
        Long id,
        @NotNull(message = "L'ID de la demande de soutenance est obligatoire")
        Long demandeSoutenanceId,
        @NotNull(message = "L'ID du directeur (proposé par) est obligatoire")
        Long proposeParId,
        @NotBlank(message = "Le nom du président est obligatoire")
        String president,
        @NotBlank(message = "Le nom du premier rapporteur est obligatoire")
        String rapporteur1,
        @NotBlank(message = "Le nom du deuxième rapporteur est obligatoire")
        String rapporteur2,
        @NotBlank(message = "Le nom du premier examinateur est obligatoire")
        String examinateur1,
        @NotBlank(message = "Le nom du deuxième examinateur est obligatoire")
        String examinateur2
) {
}
