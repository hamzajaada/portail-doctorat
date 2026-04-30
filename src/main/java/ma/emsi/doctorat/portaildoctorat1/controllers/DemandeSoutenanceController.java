package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import ma.emsi.doctorat.portaildoctorat1.dto.DemandeSoutenanceDTO;
import ma.emsi.doctorat.portaildoctorat1.services.DemandeSoutenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demandes-soutenance")
@CrossOrigin(origins = "*")
public class DemandeSoutenanceController {

    @Autowired
    private DemandeSoutenanceService demandeSoutenanceService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DemandeSoutenanceDTO demandeSoutenanceDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(demandeSoutenanceService.create(demandeSoutenanceDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(demandeSoutenanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(demandeSoutenanceService.getById(id));
    }

    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<?> getByDoctorantId(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(demandeSoutenanceService.getByDoctorantId(doctorantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DemandeSoutenanceDTO demandeSoutenanceDTO) {
        return ResponseEntity.ok(demandeSoutenanceService.update(id, demandeSoutenanceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        demandeSoutenanceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<?> changerStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(demandeSoutenanceService.changerStatut(id, statut));
    }

    @PostMapping("/verifier-prerequis")
    public ResponseEntity<?> verifierPrerequis(@RequestBody DemandeSoutenanceDTO demandeSoutenanceDTO) {
        return ResponseEntity.ok(demandeSoutenanceService.verifierPrerequis(demandeSoutenanceDTO));
    }
}
