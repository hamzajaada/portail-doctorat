package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;

    private String message;

    private boolean lu;

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "dossier_id")
    private DossierInscription dossier;
}
