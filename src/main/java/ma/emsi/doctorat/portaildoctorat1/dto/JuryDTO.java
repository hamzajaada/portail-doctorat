package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MembresDifferents
public record JuryDTO(
        @NotBlank(message = "Le nom du président est obligatoire")
        String president,
        
        @NotBlank(message = "Le nom du premier rapporteur est obligatoire")
        String rapporteur1,
        
        @NotBlank(message = "Le nom du deuxième rapporteur est obligatoire")
        String rapporteur2,
        
        @NotNull(message = "L'ID de la demande est obligatoire")
        Long demandeId
) {}
