package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.DemandeSoutenance;
import ma.emsi.doctorat.portaildoctorat1.entities.Doctorant;
import ma.emsi.doctorat.portaildoctorat1.entities.StatutSoutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeSoutenanceRepository extends JpaRepository<DemandeSoutenance, Long> {
    List<DemandeSoutenance> findByDoctorant(Doctorant doctorant);
    Optional<DemandeSoutenance> findByDoctorantAndStatutNot(Doctorant doctorant, StatutSoutenance statut);
    List<DemandeSoutenance> findByStatut(StatutSoutenance statut);
    List<DemandeSoutenance> findAllByOrderByDateDepotDesc();
    boolean existsByDoctorantAndStatutIn(Doctorant doctorant, List<StatutSoutenance> statuts);
}
