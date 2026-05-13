package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.PrerequisDTO;
import ma.emsi.doctorat.portaildoctorat1.dto.PrerequisResultat;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.Document;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.exception.PrerequisNonRemplisException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrerequisService {

    private final DemandeSoutenanceRepository demandeRepository;
    private final DoctorantRepository doctorantRepository;
    private final DossierInscriptionRepository dossierRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public PrerequisResultat verifierPrerequis(PrerequisDTO dto) {
        List<String> messagesErreur = new ArrayList<>();
        boolean publicationsOk = dto.nbPublications() != null && dto.nbPublications() >= 2;
        if (!publicationsOk) messagesErreur.add("Au moins 2 publications Q1/Q2 requises");
        
        boolean conferencesOk = dto.nbConferences() != null && dto.nbConferences() >= 2;
        if (!conferencesOk) messagesErreur.add("Au moins 2 conférences requises");
        
        boolean formationOk = dto.heuresFormation() != null && dto.heuresFormation() >= 200;
        if (!formationOk) messagesErreur.add("Au moins 200h de formation requises");
        
        boolean documentsOk = dto.fichierDemande() != null && !dto.fichierDemande().isEmpty() &&
                              dto.fichierRapportThese() != null && !dto.fichierRapportThese().isEmpty() &&
                              dto.fichierAntiPlagiat() != null && !dto.fichierAntiPlagiat().isEmpty() &&
                              dto.fichierPublications() != null && !dto.fichierPublications().isEmpty() &&
                              dto.fichierAttestations() != null && !dto.fichierAttestations().isEmpty();
                              
        if (!documentsOk) messagesErreur.add("Tous les documents obligatoires doivent être fournis au format valide (PDF ou Image)");
        
        boolean toutesConditionsRemplies = publicationsOk && conferencesOk && formationOk && documentsOk;
        
        return new PrerequisResultat(publicationsOk, conferencesOk, formationOk, documentsOk, toutesConditionsRemplies, messagesErreur);
    }

    public DemandeSoutenance soumettreDemandeAvecPrerequis(PrerequisDTO dto, Long doctorantId) {
        PrerequisResultat resultat = verifierPrerequis(dto);
        if (!resultat.toutesConditionsRemplies()) {
            throw new PrerequisNonRemplisException(resultat.messagesErreur());
        }

        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant introuvable"));

        if (demandeRepository.existsByDoctorantAndStatutIn(doctorant, List.of(
                StatutSoutenance.EN_ATTENTE,
                StatutSoutenance.PREREQUIS_VALIDES,
                StatutSoutenance.JURY_PROPOSE,
                StatutSoutenance.AUTORISEE,
                StatutSoutenance.PLANIFIEE))) {
            throw new RuntimeException("Vous avez déjà une demande de soutenance active.");
        }

        List<DossierInscription> dossiers = dossierRepository.findByDoctorant(doctorant);
        boolean aDossierValide = dossiers.stream().anyMatch(d -> d.getStatut() == StatutDossier.VALIDE_ADMIN);
        
        if (!aDossierValide) {
            throw new RuntimeException("Votre dossier d'inscription doit être validé administrativement pour soumettre une demande.");
        }

        DemandeSoutenance demande = new DemandeSoutenance();
        demande.setDoctorant(doctorant);
        demande.setNbPublications(dto.nbPublications());
        demande.setNbConferences(dto.nbConferences());
        demande.setHeuresFormation(dto.heuresFormation());
        demande.setStatut(StatutSoutenance.EN_ATTENTE);
        demande.setDateDepot(LocalDateTime.now());
        
        demande = demandeRepository.save(demande);

        sauvegarderFichier(demande, dto.fichierDemande(), "DEMANDE_SOUTENANCE", doctorantId);
        sauvegarderFichier(demande, dto.fichierRapportThese(), "RAPPORT_THESE", doctorantId);
        sauvegarderFichier(demande, dto.fichierAntiPlagiat(), "RAPPORT_ANTIPLAGIAT", doctorantId);
        sauvegarderFichier(demande, dto.fichierPublications(), "RAPPORT_PUBLICATIONS", doctorantId);
        sauvegarderFichier(demande, dto.fichierAttestations(), "ATTESTATIONS_FORMATIONS", doctorantId);

        notificationService.creerNotificationPourRole("ADMIN", 
            "Nouvelle demande de soutenance reçue de " + doctorant.getUser().getNom() + " " + doctorant.getUser().getPrenom(), 
            "/admin/soutenances/" + demande.getOid());

        return demande;
    }

    private void sauvegarderFichier(DemandeSoutenance demande, org.springframework.web.multipart.MultipartFile fichier, String type, Long doctorantId) {
        String path = fileStorageService.storeFileCustomFolder(fichier, "soutenances/" + doctorantId);
        Document doc = new Document();
        doc.setDemandeSoutenance(demande);
        doc.setNomFichier(fichier.getOriginalFilename());
        doc.setTypeDocument(type);
        doc.setCheminFichier(path);
        documentRepository.save(doc);
    }
}
