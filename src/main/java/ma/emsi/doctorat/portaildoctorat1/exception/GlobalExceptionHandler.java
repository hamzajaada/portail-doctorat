package ma.emsi.doctorat.portaildoctorat1.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", "Fichier trop grand (max 5MB)");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(PrerequisNonRemplisException.class)
    public String handlePrerequisException(PrerequisNonRemplisException exc, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("erreursPre", exc.getErreurs());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/doctorant/soutenance/nouvelle");
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException exc, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (exc.getMessage() != null && exc.getMessage().contains("Access is denied")) {
            return "redirect:/403";
        }
        redirectAttributes.addFlashAttribute("errorMessage", exc.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
