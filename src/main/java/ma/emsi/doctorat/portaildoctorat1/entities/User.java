package ma.emsi.doctorat.portaildoctorat1.entities;

import jakarta.persistence.*;
import lombok.Data;
// Elle représente une vraie Table dans la base de données PostgreSQL.
@Entity
//Souvent utilisé pour éviter les conflits avec des mots réservés en SQL comme USER
@Table(name = "utilisateur")
// Lombok Data génère automatiquement les getters, setters, toString(), equals() et hashCode()
@Data
public class User {
    // Ce champ oid est la Clé Primaire (Primary Key) de ma table
    @Id
    // Strategy: Identity signifie que la valeur sera générée automatiquement par la base de données.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;

    private String nom;
    private String prenom;
    //Indique que cette colonne doit être unique dans la table.
    @Column(unique = true)
    private String email;

    private String password;
    
    // Sur l'attribut role (qui est un Enum). 
    // On utilise EnumType.STRING pour dire à JPA : "Stocke-moi ça comme du texte (ex: ADMIN), pas comme un chiffre (ex: 0)."
    @Enumerated(EnumType.STRING)
    private Role role;  // Maintenant importé depuis Role.java
}