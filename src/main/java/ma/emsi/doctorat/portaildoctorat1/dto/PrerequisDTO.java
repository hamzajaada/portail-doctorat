package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PrerequisDTO(
        @NotNull(message="Le nombre de publications est requis")
        @Min(value=2, message="Au moins 2 publications Q1/Q2 requises")
        Integer nbPublications,
        
        @NotNull(message="Le nombre de conférences est requis")
        @Min(value=2, message="Au moins 2 conférences requises")
        Integer nbConferences,
        
        @NotNull(message="Le nombre d'heures de formation est requis")
        @Min(value=200, message="Au moins 200h de formation requises")
        Integer heuresFormation,
        
        @NotNull(message="Le fichier de demande manuscrite est obligatoire")
        MultipartFile fichierDemande,
        
        @NotNull(message="Le fichier du rapport de thèse est obligatoire")
        MultipartFile fichierRapportThese,
        
        @NotNull(message="Le fichier du rapport anti-plagiat est obligatoire")
        MultipartFile fichierAntiPlagiat,
        
        @NotNull(message="Le fichier des publications est obligatoire")
        MultipartFile fichierPublications,
        
        @NotNull(message="Le fichier des attestations de formations est obligatoire")
        MultipartFile fichierAttestations
) {}
