package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentResponseDTO(
        Long id,
        @NotNull(message = "L'ID du dossier d'inscription est obligatoire")
        Long dossierInscriptionId,
        @NotBlank(message = "Le nom du fichier est obligatoire")
        String nomFichier,
        @NotBlank(message = "Le type de document est obligatoire")
        String typeDocument,
        @NotBlank(message = "Le chemin du fichier est obligatoire")
        String cheminFichier
) {
}
