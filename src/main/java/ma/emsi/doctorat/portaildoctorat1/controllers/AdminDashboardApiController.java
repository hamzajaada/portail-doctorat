package ma.emsi.doctorat.portaildoctorat1.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.CampagneDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.repositories.CampagneRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DemandeSoutenanceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminDashboardApiController {

    private final DemandeSoutenanceRepository demandeRepository;
    private final CampagneRepository campagneRepository;

    @GetMapping("/demandes-soutenance")
    public ResponseEntity<List<DemandeSoutenance>> getAllDemandes() {
        return ResponseEntity.ok(demandeRepository.findAll());
    }

    @GetMapping("/campagnes/active")
    public ResponseEntity<CampagneDTO> getActiveCampagne() {
        return campagneRepository.findAll().stream()
                .filter(c -> c.getActive() != null && c.getActive())
                .findFirst()
                .map(c -> new CampagneDTO(c.getOid(), c.getAnneeUniv(), c.getDateOuverture(), c.getDateFermeture(), c.getActive()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null)); // Return 200 OK with null body instead of 204
    }
}
