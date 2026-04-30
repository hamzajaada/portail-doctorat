package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import ma.emsi.doctorat.portaildoctorat1.dto.JuryDTO;
import ma.emsi.doctorat.portaildoctorat1.services.JuryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jurys")
@CrossOrigin(origins = "*")
public class JuryController {

    @Autowired
    private JuryService juryService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JuryDTO juryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(juryService.create(juryDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(juryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(juryService.getById(id));
    }

    @GetMapping("/demande/{demandeId}")
    public ResponseEntity<?> getByDemandeSoutenanceId(@PathVariable Long demandeId) {
        return ResponseEntity.ok(juryService.getByDemandeSoutenanceId(demandeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody JuryDTO juryDTO) {
        return ResponseEntity.ok(juryService.update(id, juryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        juryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
