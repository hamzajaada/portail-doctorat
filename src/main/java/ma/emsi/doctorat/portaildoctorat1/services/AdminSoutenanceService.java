package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.PlanificationDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminSoutenanceService {

    private final DemandeSoutenanceRepository demandeRepository;
    private final NotificationService notificationService;
    private final ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository dossierRepository;

    public List<DemandeSoutenance> getAllDemandes() {
        return demandeRepository.findAllByOrderByDateDepotDesc();
    }

    public List<DemandeSoutenance> getDemandesByStatut(StatutSoutenance statut) {
        return demandeRepository.findByStatut(statut);
    }

    public void validerPrerequis(Long demandeId) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
        
        if (demande.getStatut() != StatutSoutenance.EN_ATTENTE) {
            throw new RuntimeException("Statut invalide pour valider les prérequis");
        }
        
        demande.setStatut(StatutSoutenance.PREREQUIS_VALIDES);
        demandeRepository.save(demande);
        
        List<ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription> dossiers = dossierRepository.findByDoctorant(demande.getDoctorant());
        if (!dossiers.isEmpty()) {
            Long directeurId = dossiers.get(0).getDirecteur().getUser().getOid();
            notificationService.creerNotification(directeurId, 
                "Veuillez proposer un jury pour la soutenance de " + demande.getDoctorant().getUser().getNom(), 
                "/directeur/soutenances");
        }
    }

    public void autoriserSoutenance(Long demandeId) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
                
        if (demande.getStatut() != StatutSoutenance.JURY_PROPOSE) {
            throw new RuntimeException("Un jury doit être proposé avant l'autorisation");
        }
        
        demande.setStatut(StatutSoutenance.AUTORISEE);
        demandeRepository.save(demande);
        
        notificationService.creerNotification(demande.getDoctorant().getUser().getOid(), 
            "Votre demande de soutenance est autorisée. En attente de planification.", 
            "/doctorant/soutenance/suivi");
            
        List<ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription> dossiers = dossierRepository.findByDoctorant(demande.getDoctorant());
        if (!dossiers.isEmpty()) {
            Long directeurId = dossiers.get(0).getDirecteur().getUser().getOid();
            notificationService.creerNotification(directeurId, 
                "La soutenance de " + demande.getDoctorant().getUser().getNom() + " a été autorisée.", 
                "/directeur/soutenances");
        }
    }

    public void planifierSoutenance(Long demandeId, PlanificationDTO dto) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
                
        if (demande.getStatut() != StatutSoutenance.AUTORISEE) {
            throw new RuntimeException("La demande doit être autorisée avant planification");
        }
        
        demande.setDateSoutenance(dto.dateSoutenance());
        demande.setLieuSoutenance(dto.lieuSoutenance());
        demande.setStatut(StatutSoutenance.PLANIFIEE);
        demandeRepository.save(demande);
        
        notificationService.creerNotification(demande.getDoctorant().getUser().getOid(), 
            "Votre soutenance a été planifiée le " + dto.dateSoutenance() + " à " + dto.lieuSoutenance(), 
            "/doctorant/soutenance/suivi");
            
        List<ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription> dossiers = dossierRepository.findByDoctorant(demande.getDoctorant());
        if (!dossiers.isEmpty()) {
            Long directeurId = dossiers.get(0).getDirecteur().getUser().getOid();
            notificationService.creerNotification(directeurId, 
                "La soutenance de " + demande.getDoctorant().getUser().getNom() + " a été planifiée.", 
                "/directeur/soutenances");
        }
    }

    public void rejeterDemande(Long demandeId, String motif) {
        DemandeSoutenance demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
                
        demande.setStatut(StatutSoutenance.REJETEE);
        demandeRepository.save(demande);
        
        notificationService.creerNotification(demande.getDoctorant().getUser().getOid(), 
            "Votre demande de soutenance a été rejetée. Motif : " + motif, 
            "/doctorant/soutenance/suivi");
    }
}
