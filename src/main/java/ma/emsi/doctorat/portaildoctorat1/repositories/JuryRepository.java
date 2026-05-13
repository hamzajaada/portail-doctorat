package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuryRepository extends JpaRepository<Jury, Long> {
    Optional<Jury> findByDemandeSoutenance(DemandeSoutenance demandeSoutenance);
    Optional<Jury> findByDemandeSoutenance_Oid(Long demandeId);
    boolean existsByDemandeSoutenance_Oid(Long demandeId);
}
