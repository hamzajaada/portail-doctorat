package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "utilisateur")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;  // Maintenant importé depuis Role.java
}