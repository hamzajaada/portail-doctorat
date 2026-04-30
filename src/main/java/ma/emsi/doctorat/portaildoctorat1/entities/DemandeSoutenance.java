package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "demande_soutenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeSoutenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @ManyToOne
    @JoinColumn(name = "doctorant_id")
    private Doctorant doctorant;

    private int nbPublications;
    private int nbConferences;
    private int heuresFormation;
    private String statut;
    private LocalDateTime dateSoutenance;
    private String lieuSoutenance;
}
