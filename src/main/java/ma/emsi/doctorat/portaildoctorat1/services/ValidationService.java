package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ValidationService {

    private final DossierInscriptionRepository dossierRepository;
    private final NotificationService notificationService;

    public void validerParDirecteur(Long dossierId, Long directeurId, String avis) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));
                
        if (!dossier.getDirecteur().getOid().equals(directeurId)) {
            throw new RuntimeException("Vous n'êtes pas le directeur de ce dossier");
        }
        
        dossier.setStatut(StatutDossier.VALIDE_DIRECTEUR);
        // On pourrait stocker l'avis dans un champ, mais simplifions pour l'instant
        dossierRepository.save(dossier);
        notificationService.notifierAdmin(dossier);
    }

    public void rejeterParDirecteur(Long dossierId, Long directeurId, String motif) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        if (!dossier.getDirecteur().getOid().equals(directeurId)) {
            throw new RuntimeException("Vous n'êtes pas le directeur de ce dossier");
        }

        dossier.setStatut(StatutDossier.REJETE);
        dossierRepository.save(dossier);
        notificationService.notifierDoctorant(dossier, "Votre dossier a été rejeté par votre directeur. Motif : " + motif);
    }

    public void validerParAdmin(Long dossierId) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        dossier.setStatut(StatutDossier.VALIDE_ADMIN);
        dossierRepository.save(dossier);
        notificationService.notifierDoctorant(dossier, "Félicitations, votre dossier a été validé par l'administration.");
    }

    public void rejeterParAdmin(Long dossierId, String motif) {
        DossierInscription dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        dossier.setStatut(StatutDossier.REJETE);
        dossierRepository.save(dossier);
        notificationService.notifierDoctorant(dossier, "Votre dossier a été rejeté par l'administration. Motif : " + motif);
    }
}
