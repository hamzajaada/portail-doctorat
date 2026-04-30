package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeSoutenanceRepository extends JpaRepository<DemandeSoutenance, Long> {

    @Query("SELECT d FROM DemandeSoutenance d WHERE d.doctorant.oid = :doctorantId")
    Optional<DemandeSoutenance> findByDoctorantId(@Param("doctorantId") Long doctorantId);

    List<DemandeSoutenance> findByStatut(String statut);
}
