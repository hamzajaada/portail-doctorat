package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @ManyToOne
    @JoinColumn(name = "dossier_inscription_id")
    private DossierInscription dossierInscription;

    private String nomFichier;
    private String typeDocument;
    private String cheminFichier;
}
