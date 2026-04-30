package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.CampagneDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.CampagneRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CampagneService {

    private final CampagneRepository campagneRepository;
    private final DossierInscriptionRepository dossierRepository;

    public CampagneDTO createCampagne(CampagneDTO dto) {
        if (dto.active() != null && dto.active()) {
            if (campagneRepository.findByActiveTrue().isPresent()) {
                throw new RuntimeException("Il existe déjà une campagne active. Désactivez-la d'abord.");
            }
        }
        if (dto.dateOuverture() != null && dto.dateFermeture() != null && !dto.dateOuverture().isBefore(dto.dateFermeture())) {
            throw new IllegalArgumentException("La date d'ouverture doit être avant la date de fermeture");
        }
        Campagne campagne = new Campagne();
        campagne.setAnneeUniv(dto.anneeUniv());
        campagne.setDateOuverture(dto.dateOuverture());
        campagne.setDateFermeture(dto.dateFermeture());
        campagne.setActive(dto.active() != null ? dto.active() : false);

        return mapToDTO(campagneRepository.save(campagne));
    }

    @Transactional(readOnly = true)
    public CampagneDTO getCampagneById(Long id) {
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'id : " + id));
        return mapToDTO(campagne);
    }

    @Transactional(readOnly = true)
    public List<CampagneDTO> getAllCampagnes() {
        return campagneRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public CampagneDTO updateCampagne(Long id, CampagneDTO dto) {
        if (dto.dateOuverture() != null && dto.dateFermeture() != null && !dto.dateOuverture().isBefore(dto.dateFermeture())) {
            throw new IllegalArgumentException("La date d'ouverture doit être avant la date de fermeture");
        }
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'id : " + id));
        campagne.setAnneeUniv(dto.anneeUniv());
        campagne.setDateOuverture(dto.dateOuverture());
        campagne.setDateFermeture(dto.dateFermeture());
        if (dto.active() != null) campagne.setActive(dto.active());
        return mapToDTO(campagneRepository.save(campagne));
    }

    public void deleteCampagne(Long id) {
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée"));
        if (!dossierRepository.findByCampagneId(id).isEmpty()) {
            throw new RuntimeException("Impossible de supprimer la campagne : des dossiers y sont liés.");
        }
        campagneRepository.deleteById(id);
    }

    public CampagneDTO toggleActivation(Long id) {
        Campagne campagne = campagneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campagne non trouvée avec l'id : " + id));
        
        if (!campagne.getActive()) {
            List<Campagne> allCampagnes = campagneRepository.findAll();
            allCampagnes.forEach(c -> c.setActive(false));
            campagneRepository.saveAll(allCampagnes);
        }
        
        campagne.setActive(!campagne.getActive());
        return mapToDTO(campagneRepository.save(campagne));
    }

    private CampagneDTO mapToDTO(Campagne campagne) {
        return new CampagneDTO(campagne.getOid(), campagne.getAnneeUniv(), campagne.getDateOuverture(), campagne.getDateFermeture(), campagne.getActive());
    }
}
