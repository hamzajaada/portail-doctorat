package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.dossierInscription.oid = :dossierId")
    List<Document> findByDossierInscriptionId(@Param("dossierId") Long dossierId);

    List<Document> findByDossierInscription(ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription dossier);
    List<Document> findByDossierInscriptionAndTypeDocument(ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription dossier, String type);
}
