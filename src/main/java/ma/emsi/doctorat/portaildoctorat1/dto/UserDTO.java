package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ma.emsi.doctorat.portaildoctorat1.entities.Role;

public record UserDTO(
                Long id,
                // @NotBlank est une annotation de validation de Spring qui vérifie qu'un champ
                // n'est pas nul, pas vide et pas composé uniquement d'espaces.
                @NotBlank(message = "Le nom est obligatoire") String nom,
                @NotBlank(message = "Le prénom est obligatoire") String prenom,
                @NotBlank(message = "L'email est obligatoire") @Email(message = "Format d'email invalide") String email,
                @NotBlank(message = "Le mot de passe est obligatoire") @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String password,
                @NotNull(message = "Le rôle est obligatoire") Role role) {
}
