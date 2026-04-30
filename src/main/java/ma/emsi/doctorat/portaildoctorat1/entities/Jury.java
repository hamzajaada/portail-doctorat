package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jury")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @OneToOne
    @JoinColumn(name = "demande_soutenance_id")
    private DemandeSoutenance demandeSoutenance;

    @ManyToOne
    @JoinColumn(name = "propose_par_id")
    private Directeur proposePar;

    private String president;
    private String rapporteur1;
    private String rapporteur2;
    private String examinateur1;
    private String examinateur2;
}
