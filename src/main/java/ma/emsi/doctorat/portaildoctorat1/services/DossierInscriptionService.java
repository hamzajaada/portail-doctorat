package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DossierInscriptionDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.CampagneRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DirecteurRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DossierInscriptionService {

    private final DossierInscriptionRepository dossierRepository;
    private final DoctorantRepository doctorantRepository;
    private final DirecteurRepository directeurRepository;
    private final CampagneRepository campagneRepository;

    public DossierInscriptionDTO create(DossierInscriptionDTO dto) {
        Campagne campagne = campagneRepository.findById(dto.campagneId())
                .orElseThrow(() -> new ResourceNotFoundException("Campagne introuvable"));
        
        LocalDate now = LocalDate.now();
        if (!Boolean.TRUE.equals(campagne.getActive()) || 
            campagne.getDateOuverture() == null || campagne.getDateFermeture() == null ||
            now.isBefore(campagne.getDateOuverture()) || now.isAfter(campagne.getDateFermeture())) {
            throw new IllegalArgumentException("La campagne doit être active et la date courante doit être entre la date d'ouverture et la date de fermeture pour créer un dossier");
        }

        if (dossierRepository.findByDoctorantIdAndCampagneId(dto.doctorantId(), dto.campagneId()).isPresent()) {
            throw new IllegalArgumentException("Le doctorant a déjà un dossier pour cette campagne");
        }

        Doctorant doctorant = doctorantRepository.findById(dto.doctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable"));
        Directeur directeur = directeurRepository.findById(dto.directeurId())
                .orElseThrow(() -> new ResourceNotFoundException("Directeur introuvable"));

        DossierInscription dossier = new DossierInscription();
        dossier.setDoctorant(doctorant);
        dossier.setDirecteur(directeur);
        dossier.setCampagne(campagne);
        dossier.setSujetThese(dto.sujetThese());
        dossier.setStatut(ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier.BROUILLON);

        return mapToDTO(dossierRepository.save(dossier));
    }

    @Transactional(readOnly = true)
    public DossierInscriptionDTO getById(Long id) {
        return mapToDTO(dossierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable")));
    }

    @Transactional(readOnly = true)
    public List<DossierInscriptionDTO> getAll() {
        return dossierRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DossierInscriptionDTO> getByDoctorantId(Long doctorantId) {
        return dossierRepository.findByDoctorantId(doctorantId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DossierInscriptionDTO> getByDirecteurId(Long directeurId) {
        return dossierRepository.findByDirecteurId(directeurId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public DossierInscriptionDTO update(Long id, DossierInscriptionDTO dto) {
        DossierInscription dossier = dossierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable"));
        dossier.setSujetThese(dto.sujetThese());
        return mapToDTO(dossierRepository.save(dossier));
    }

    public void delete(Long id) {
        dossierRepository.deleteById(id);
    }

    public DossierInscriptionDTO changerStatut(Long id, String statut) {
        DossierInscription dossier = dossierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable"));
        dossier.setStatut(ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier.valueOf(statut));
        return mapToDTO(dossierRepository.save(dossier));
    }

    private DossierInscriptionDTO mapToDTO(DossierInscription dossier) {
        String nom = null, prenom = null, email = null;
        if (dossier.getDoctorant() != null && dossier.getDoctorant().getUser() != null) {
            nom = dossier.getDoctorant().getUser().getNom();
            prenom = dossier.getDoctorant().getUser().getPrenom();
            email = dossier.getDoctorant().getUser().getEmail();
        }

        return new DossierInscriptionDTO(
                dossier.getOid(),
                dossier.getDoctorant() != null ? dossier.getDoctorant().getOid() : null,
                dossier.getDirecteur() != null ? dossier.getDirecteur().getOid() : null,
                dossier.getCampagne() != null ? dossier.getCampagne().getOid() : null,
                dossier.getSujetThese(),
                nom,
                prenom,
                email,
                dossier.getStatut() != null ? dossier.getStatut().name() : null,
                dossier.getDateDepot()
        );
    }
}
