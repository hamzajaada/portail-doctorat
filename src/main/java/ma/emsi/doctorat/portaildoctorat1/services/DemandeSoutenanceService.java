package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DemandeSoutenanceDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DoctorantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeSoutenanceService {

    private final DemandeSoutenanceRepository demandeRepository;
    private final DoctorantRepository doctorantRepository;

    public DemandeSoutenanceDTO create(DemandeSoutenanceDTO dto) {
        if (!verifierPrerequis(dto)) {
            throw new IllegalArgumentException("Les prérequis ne sont pas validés (nbPublications >= 2, nbConferences >= 2, heuresFormation >= 200)");
        }

        Doctorant doctorant = doctorantRepository.findById(dto.doctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable"));

        DemandeSoutenance demande = new DemandeSoutenance();
        demande.setDoctorant(doctorant);
        demande.setNbPublications(dto.nbPublications());
        demande.setNbConferences(dto.nbConferences());
        demande.setHeuresFormation(dto.heuresFormation());
        demande.setStatut("EN_ATTENTE");
        demande.setDateSoutenance(dto.dateSoutenance());
        demande.setLieuSoutenance(dto.lieuSoutenance());

        return mapToDTO(demandeRepository.save(demande));
    }

    @Transactional(readOnly = true)
    public DemandeSoutenanceDTO getById(Long id) {
        return mapToDTO(demandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Demande introuvable")));
    }

    @Transactional(readOnly = true)
    public List<DemandeSoutenanceDTO> getAll() {
        return demandeRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DemandeSoutenanceDTO getByDoctorantId(Long doctorantId) {
        DemandeSoutenance demande = demandeRepository.findByDoctorantId(doctorantId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable pour ce doctorant"));
        return mapToDTO(demande);
    }

    public DemandeSoutenanceDTO update(Long id, DemandeSoutenanceDTO dto) {
        DemandeSoutenance demande = demandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        demande.setNbPublications(dto.nbPublications());
        demande.setNbConferences(dto.nbConferences());
        demande.setHeuresFormation(dto.heuresFormation());
        demande.setDateSoutenance(dto.dateSoutenance());
        demande.setLieuSoutenance(dto.lieuSoutenance());
        return mapToDTO(demandeRepository.save(demande));
    }

    public void delete(Long id) {
        demandeRepository.deleteById(id);
    }

    public DemandeSoutenanceDTO changerStatut(Long id, String statut) {
        DemandeSoutenance demande = demandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Demande introuvable"));
        demande.setStatut(statut);
        return mapToDTO(demandeRepository.save(demande));
    }

    public boolean verifierPrerequis(DemandeSoutenanceDTO dto) {
        return dto.nbPublications() >= 2 && dto.nbConferences() >= 2 && dto.heuresFormation() >= 200;
    }

    private DemandeSoutenanceDTO mapToDTO(DemandeSoutenance demande) {
        return new DemandeSoutenanceDTO(
                demande.getOid(),
                demande.getDoctorant() != null ? demande.getDoctorant().getOid() : null,
                demande.getNbPublications(),
                demande.getNbConferences(),
                demande.getHeuresFormation(),
                demande.getStatut(),
                demande.getDateSoutenance(),
                demande.getLieuSoutenance()
        );
    }
}
