package ma.emsi.doctorat.portaildoctorat1.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentDTO(
        @NotBlank(message = "Le type de document est obligatoire")
        String typeDocument,
        @NotNull(message = "Le fichier est obligatoire")
        MultipartFile fichier
) {
}
