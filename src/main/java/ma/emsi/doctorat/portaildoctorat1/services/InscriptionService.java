package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.Document;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.repositories.CampagneRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.dto.DossierDTO;
import ma.emsi.doctorat.portaildoctorat1.dto.DocumentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InscriptionService {

    private final CampagneRepository campagneRepository;
    private final DirecteurRepository directeurRepository;
    private final DoctorantRepository doctorantRepository;
    private final DossierInscriptionRepository dossierRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public Campagne getCampagneActive() {
        return campagneRepository.findByActiveTrue()
                .orElseThrow(() -> new RuntimeException("Aucune campagne active ouverte"));
    }

    public List<Directeur> getDirecteursDisponibles() {
        return directeurRepository.findAll();
    }

    public DossierInscription soumettreInscription(DossierDTO dto, Long doctorantId) {
        Campagne campagne = getCampagneActive();
        LocalDate now = LocalDate.now();
        if (now.isBefore(campagne.getDateOuverture()) || now.isAfter(campagne.getDateFermeture())) {
            throw new RuntimeException("La campagne n'est pas ouverte pour soumission à cette date.");
        }

        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant introuvable"));

        if (dossierRepository.findByDoctorantAndCampagne(doctorant, campagne).isPresent()) {
            throw new RuntimeException("Vous avez déjà un dossier pour cette campagne.");
        }

        if (doctorant.getDatePremiereInscription() != null) {
            long months = ChronoUnit.MONTHS.between(doctorant.getDatePremiereInscription().withDayOfMonth(1), now.withDayOfMonth(1));
            if (months > 36) { // > 3 years
                throw new RuntimeException("Durée maximale de 3 ans dépassée. Contactez le PED.");
            }
            // Warning logic 6 years can be checked via controller or DTO later
        }

        Directeur directeur = directeurRepository.findById(dto.directeurId())
                .orElseThrow(() -> new RuntimeException("Directeur introuvable"));

        DossierInscription dossier = new DossierInscription();
        dossier.setDoctorant(doctorant);
        dossier.setDirecteur(directeur);
        dossier.setCampagne(campagne);
        dossier.setSujetThese(dto.sujetThese());
        dossier.setStatut(StatutDossier.BROUILLON);

        return dossierRepository.save(dossier);
    }

    public void finaliserSoumission(Long dossierId) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));
        dossier.setStatut(StatutDossier.SOUMIS);
        dossierRepository.save(dossier);
        notificationService.notifierDirecteur(dossier);
    }

    public void sauvegarderDocument(Long dossierId, DocumentDTO dto) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        String path = fileStorageService.storeFile(dto.fichier(), dossierId);
        
        Document document = new Document();
        document.setDossierInscription(dossier);
        document.setNomFichier(dto.fichier().getOriginalFilename());
        document.setTypeDocument(dto.typeDocument());
        document.setCheminFichier(path);
        
        documentRepository.save(document);
    }
}
