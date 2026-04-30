package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.DossierInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierInscriptionRepository extends JpaRepository<DossierInscription, Long> {

    @Query("SELECT d FROM DossierInscription d WHERE d.doctorant.oid = :doctorantId")
    List<DossierInscription> findByDoctorantId(@Param("doctorantId") Long doctorantId);

    @Query("SELECT d FROM DossierInscription d WHERE d.directeur.oid = :directeurId")
    List<DossierInscription> findByDirecteurId(@Param("directeurId") Long directeurId);

    @Query("SELECT d FROM DossierInscription d WHERE d.campagne.oid = :campagneId")
    List<DossierInscription> findByCampagneId(@Param("campagneId") Long campagneId);

    List<DossierInscription> findByStatut(String statut);

    @Query("SELECT d FROM DossierInscription d WHERE d.doctorant.oid = :doctorantId AND d.campagne.oid = :campagneId")
    Optional<DossierInscription> findByDoctorantIdAndCampagneId(@Param("doctorantId") Long doctorantId, @Param("campagneId") Long campagneId);

    List<DossierInscription> findByDoctorant(ma.emsi.doctorat.portaildoctorat1.entities.Doctorant doctorant);
    Optional<DossierInscription> findByDoctorantAndCampagne(ma.emsi.doctorat.portaildoctorat1.entities.Doctorant doctorant, ma.emsi.doctorat.portaildoctorat1.entities.Campagne campagne);
    List<DossierInscription> findByStatut(ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier statut);
    List<DossierInscription> findByDirecteur(ma.emsi.doctorat.portaildoctorat1.entities.Directeur directeur);
    long countByDoctorantAndStatutNot(ma.emsi.doctorat.portaildoctorat1.entities.Doctorant doctorant, ma.emsi.doctorat.portaildoctorat1.entities.StatutDossier statut);
}
