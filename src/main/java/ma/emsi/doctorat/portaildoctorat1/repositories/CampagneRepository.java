package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.Campagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    List<Campagne> findByActiveTrue();
    Optional<Campagne> findByAnneeUniv(String anneeUniv);
    List<Campagne> findAllByOrderByDateOuvertureDesc();
    boolean existsByAnneeUnivAndActiveTrue(String anneeUniv);
}
