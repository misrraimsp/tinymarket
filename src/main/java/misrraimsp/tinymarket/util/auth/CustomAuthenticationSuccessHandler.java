package misrraimsp.tinymarket.util.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = this.determineTargetUrl(authentication);
        LOGGER.info("User redirected to {}", targetUrl);
        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request);
    }


    private String determineTargetUrl(final Authentication authentication) {
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_USER", "/user/products");
        roleTargetUrlMap.put("ROLE_ADMIN", "/admin/pedidos");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (roleTargetUrlMap.containsKey(authorityName)) {
                LOGGER.info("User authenticated with role {}", authorityName);
                return roleTargetUrlMap.get(authorityName);
            }
        }
        LOGGER.error("User authenticated with no recognized role: {}", authorities);
        throw new IllegalStateException();
    }

}
