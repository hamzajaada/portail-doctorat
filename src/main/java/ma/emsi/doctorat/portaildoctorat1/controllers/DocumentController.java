package ma.emsi.doctorat.portaildoctorat1.controllers;

import jakarta.validation.Valid;
import ma.emsi.doctorat.portaildoctorat1.dto.DocumentResponseDTO;
import ma.emsi.doctorat.portaildoctorat1.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody DocumentResponseDTO documentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(documentDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(documentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getById(id));
    }

    @GetMapping("/dossier/{dossierId}")
    public ResponseEntity<?> getByDossierId(@PathVariable Long dossierId) {
        return ResponseEntity.ok(documentService.getByDossierId(dossierId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
