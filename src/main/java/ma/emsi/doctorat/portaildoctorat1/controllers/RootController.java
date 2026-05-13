package ma.emsi.doctorat.portaildoctorat1.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else if (authority.getAuthority().equals("ROLE_DIRECTEUR")) {
                    return "redirect:/directeur/dashboard";
                } else if (authority.getAuthority().equals("ROLE_DOCTORANT")) {
                    return "redirect:/doctorant/dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}
