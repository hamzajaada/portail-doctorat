package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.JuryDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.JuryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JuryService {

    private final DemandeSoutenanceRepository demandeRepository;
    private final JuryRepository juryRepository;
    private final DirecteurRepository directeurRepository;
    private final NotificationService notificationService;
    private final ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository dossierRepository;

    public List<DemandeSoutenance> getDemandesEnAttentePourDirecteur(Long directeurId) {
        Directeur directeur = directeurRepository.findById(directeurId)
                .orElseThrow(() -> new RuntimeException("Directeur introuvable"));
                
        List<DemandeSoutenance> toutes = demandeRepository.findByStatut(StatutSoutenance.PREREQUIS_VALIDES);
        return toutes.stream()
                .filter(d -> {
                    return dossierRepository.findByDoctorant(d.getDoctorant()).stream()
                            .anyMatch(dossier -> dossier.getDirecteur().getOid().equals(directeurId));
                })
                .collect(Collectors.toList());
    }

    public Jury proposerJury(JuryDTO dto, Long directeurId) {
        Directeur directeur = directeurRepository.findById(directeurId)
                .orElseThrow(() -> new RuntimeException("Directeur introuvable"));
                
        DemandeSoutenance demande = demandeRepository.findById(dto.demandeId())
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
                
        if (juryRepository.existsByDemandeSoutenance_Oid(dto.demandeId())) {
            throw new RuntimeException("Un jury a déjà été proposé pour cette demande");
        }
        
        Jury jury = new Jury();
        jury.setDemandeSoutenance(demande);
        jury.setProposePar(directeur);
        jury.setPresident(dto.president());
        jury.setRapporteur1(dto.rapporteur1());
        jury.setRapporteur2(dto.rapporteur2());
        juryRepository.save(jury);
        
        demande.setStatut(StatutSoutenance.JURY_PROPOSE);
        demandeRepository.save(demande);
        
        notificationService.creerNotificationPourRole("ADMIN", 
            "Un jury a été proposé pour la soutenance de " + demande.getDoctorant().getUser().getNom(), 
            "/admin/soutenances/" + demande.getOid());
            
        return jury;
    }

    public Jury modifierJury(Long juryId, JuryDTO dto, Long directeurId) {
        Jury jury = juryRepository.findById(juryId)
                .orElseThrow(() -> new RuntimeException("Jury introuvable"));
                
        if (jury.getDemandeSoutenance().getStatut() != StatutSoutenance.JURY_PROPOSE) {
            throw new RuntimeException("Impossible de modifier le jury car la soutenance a déjà dépassé l'étape de proposition de jury");
        }
        
        jury.setPresident(dto.president());
        jury.setRapporteur1(dto.rapporteur1());
        jury.setRapporteur2(dto.rapporteur2());
        
        return juryRepository.save(jury);
    }
}
