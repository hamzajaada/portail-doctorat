package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dossier_inscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DossierInscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    @ManyToOne
    @JoinColumn(name = "doctorant_id")
    private Doctorant doctorant;

    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private Directeur directeur;

    @ManyToOne
    @JoinColumn(name = "campagne_id")
    private Campagne campagne;

    private String sujetThese;
    
    @Enumerated(EnumType.STRING)
    private StatutDossier statut;

    @CreationTimestamp
    private LocalDateTime dateDepot;
}
