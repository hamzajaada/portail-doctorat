package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import ma.emsi.doctorat.portaildoctorat1.dto.DoctorantDTO;
import ma.emsi.doctorat.portaildoctorat1.services.DoctorantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctorants")
@CrossOrigin(origins = "*")
public class DoctorantController {

    @Autowired
    private DoctorantService doctorantService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DoctorantDTO doctorantDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorantService.createDoctorant(doctorantDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(doctorantService.getAllDoctorants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorantService.getDoctorantById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByUserEmail(@PathVariable String email) {
        return ResponseEntity.ok(doctorantService.getDoctorantByUserEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody DoctorantDTO doctorantDTO) {
        return ResponseEntity.ok(doctorantService.updateDoctorant(id, doctorantDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        doctorantService.deleteDoctorant(id);
        return ResponseEntity.ok().build();
    }
}