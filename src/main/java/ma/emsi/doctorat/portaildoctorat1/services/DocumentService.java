package ma.emsi.doctorat.portaildoctorat1.services;

import lombok.RequiredArgsConstructor;
import ma.emsi.doctorat.portaildoctorat1.dto.DocumentResponseDTO;
import ma.emsi.doctorat.portaildoctorat1.entities.Document;
import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import ma.emsi.doctorat.portaildoctorat1.exception.ResourceNotFoundException;
import ma.emsi.doctorat.portaildoctorat1.repositories.DocumentRepository;
import ma.emsi.doctorat.portaildoctorat1.repositories.DossierInscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DossierInscriptionRepository dossierRepository;

    public DocumentResponseDTO create(DocumentResponseDTO dto) {
        DossierInscription dossier = dossierRepository.findById(dto.dossierInscriptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable"));

        Document doc = new Document();
        doc.setDossierInscription(dossier);
        doc.setNomFichier(dto.nomFichier());
        doc.setTypeDocument(dto.typeDocument());
        doc.setCheminFichier(dto.cheminFichier());

        return mapToDTO(documentRepository.save(doc));
    }

    @Transactional(readOnly = true)
    public DocumentResponseDTO getById(Long id) {
        return mapToDTO(documentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Document introuvable")));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getAll() {
        return documentRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentResponseDTO> getByDossierId(Long dossierId) {
        return documentRepository.findByDossierInscriptionId(dossierId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    private DocumentResponseDTO mapToDTO(Document doc) {
        return new DocumentResponseDTO(
                doc.getOid(),
                doc.getDossierInscription() != null ? doc.getDossierInscription().getOid() : null,
                doc.getNomFichier(),
                doc.getTypeDocument(),
                doc.getCheminFichier()
        );
    }
}
