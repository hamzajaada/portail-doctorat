package ma.emsi.doctorat.portaildoctorat1.dto;

import java.util.List;

public record PrerequisResultat(
        boolean publicationsOk,
        boolean conferencesOk,
        boolean formationOk,
        boolean documentsOk,
        boolean toutesConditionsRemplies,
        List<String> messagesErreur
) {}
