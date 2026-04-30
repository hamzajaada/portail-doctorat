package ma.emsi.doctorat.portaildoctorat1.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/";

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/ui/dashboard/admin";
                break;
            } else if (authority.getAuthority().equals("ROLE_DIRECTEUR")) {
                redirectUrl = "/ui/dashboard/directeur";
                break;
            } else if (authority.getAuthority().equals("ROLE_DOCTORANT")) {
                redirectUrl = "/ui/dashboard/doctorant";
                break;
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
