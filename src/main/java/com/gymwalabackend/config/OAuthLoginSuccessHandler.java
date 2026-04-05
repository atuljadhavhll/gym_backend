package com.gymwalabackend.config;

import com.gymwalabackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getAttribute("email"));
        claims.put("name", user.getAttribute("name"));
        claims.put("given_name", user.getAttribute("given_name"));
        claims.put("family_name", user.getAttribute("family_name"));
        claims.put("picture", user.getAttribute("picture"));

        String token = jwtUtil.generateToken(claims);

        String redirectUrl = "http://localhost:4200/oauth-success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
