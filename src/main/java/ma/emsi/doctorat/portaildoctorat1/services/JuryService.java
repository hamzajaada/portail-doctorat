package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.JuryDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
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

    private final JuryRepository juryRepository;
    private final DemandeSoutenanceRepository demandeRepository;
    private final DirecteurRepository directeurRepository;

    public JuryDTO create(JuryDTO dto) {
        DemandeSoutenance demande = demandeRepository.findById(dto.demandeSoutenanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande de soutenance introuvable"));
        Directeur proposePar = directeurRepository.findById(dto.proposeParId())
                .orElseThrow(() -> new ResourceNotFoundException("Directeur introuvable"));

        Jury jury = new Jury();
        jury.setDemandeSoutenance(demande);
        jury.setProposePar(proposePar);
        jury.setPresident(dto.president());
        jury.setRapporteur1(dto.rapporteur1());
        jury.setRapporteur2(dto.rapporteur2());
        jury.setExaminateur1(dto.examinateur1());
        jury.setExaminateur2(dto.examinateur2());

        return mapToDTO(juryRepository.save(jury));
    }

    @Transactional(readOnly = true)
    public JuryDTO getById(Long id) {
        return mapToDTO(juryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jury introuvable")));
    }

    @Transactional(readOnly = true)
    public List<JuryDTO> getAll() {
        return juryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JuryDTO getByDemandeSoutenanceId(Long demandeId) {
        Jury jury = juryRepository.findByDemandeSoutenanceId(demandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Jury introuvable pour cette demande"));
        return mapToDTO(jury);
    }

    public JuryDTO update(Long id, JuryDTO dto) {
        Jury jury = juryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jury introuvable"));
        jury.setPresident(dto.president());
        jury.setRapporteur1(dto.rapporteur1());
        jury.setRapporteur2(dto.rapporteur2());
        jury.setExaminateur1(dto.examinateur1());
        jury.setExaminateur2(dto.examinateur2());
        return mapToDTO(juryRepository.save(jury));
    }

    public void delete(Long id) {
        juryRepository.deleteById(id);
    }

    private JuryDTO mapToDTO(Jury jury) {
        return new JuryDTO(
                jury.getOid(),
                jury.getDemandeSoutenance() != null ? jury.getDemandeSoutenance().getOid() : null,
                jury.getProposePar() != null ? jury.getProposePar().getOid() : null,
                jury.getPresident(),
                jury.getRapporteur1(),
                jury.getRapporteur2(),
                jury.getExaminateur1(),
                jury.getExaminateur2()
        );
    }
}
