package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import ma.emsi.doctorat.portaildoctorat1.dto.DossierInscriptionDTO;
import ma.emsi.doctorat.portaildoctorat1.services.DossierInscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dossiers")
@CrossOrigin(origins = "*")
public class DossierInscriptionController {

    @Autowired
    private DossierInscriptionService dossierInscriptionService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DossierInscriptionDTO dossierInscriptionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dossierInscriptionService.create(dossierInscriptionDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(dossierInscriptionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(dossierInscriptionService.getById(id));
    }

    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<?> getByDoctorantId(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(dossierInscriptionService.getByDoctorantId(doctorantId));
    }

    @GetMapping("/directeur/{directeurId}")
    public ResponseEntity<?> getByDirecteurId(@PathVariable Long directeurId) {
        return ResponseEntity.ok(dossierInscriptionService.getByDirecteurId(directeurId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DossierInscriptionDTO dossierInscriptionDTO) {
        return ResponseEntity.ok(dossierInscriptionService.update(id, dossierInscriptionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        dossierInscriptionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<?> changerStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(dossierInscriptionService.changerStatut(id, statut));
    }
}
