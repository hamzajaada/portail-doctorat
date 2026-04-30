package ma.emsi.doctorat.portaildoctorat1.repositories;

import ma.emsi.doctorat.portaildoctorat1.entities.Directeur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DirecteurRepository extends JpaRepository<Directeur, Long> {
    Optional<Directeur> findByUser_Email(String email);
}
