package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuryRepository extends JpaRepository<Jury, Long> {

    @Query("SELECT j FROM Jury j WHERE j.demandeSoutenance.oid = :demandeId")
    Optional<Jury> findByDemandeSoutenanceId(@Param("demandeId") Long demandeId);
}
