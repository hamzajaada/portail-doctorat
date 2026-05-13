package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class MembresDifferentsValidator implements ConstraintValidator<MembresDifferents, JuryDTO> {
    @Override
    public boolean isValid(JuryDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;
        Set<String> membres = new HashSet<>();
        
        if (dto.president() != null && !dto.president().isBlank()) {
            membres.add(dto.president().trim().toLowerCase());
        }
        if (dto.rapporteur1() != null && !dto.rapporteur1().isBlank()) {
            if (!membres.add(dto.rapporteur1().trim().toLowerCase())) return false;
        }
        if (dto.rapporteur2() != null && !dto.rapporteur2().isBlank()) {
            if (!membres.add(dto.rapporteur2().trim().toLowerCase())) return false;
        }
        return true;
    }
}
