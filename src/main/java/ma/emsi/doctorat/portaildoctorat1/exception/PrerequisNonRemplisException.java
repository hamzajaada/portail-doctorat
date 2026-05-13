package ma.emsi.doctorat.portaildoctorat1.exception;

import java.util.List;

public class PrerequisNonRemplisException extends RuntimeException {
    private final List<String> erreurs;

    public PrerequisNonRemplisException(List<String> erreurs) {
        super("Les prérequis ne sont pas remplis");
        this.erreurs = erreurs;
    }

    public List<String> getErreurs() {
        return erreurs;
    }
}
