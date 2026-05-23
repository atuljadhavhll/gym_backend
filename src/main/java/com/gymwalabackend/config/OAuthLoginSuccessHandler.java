package com.gymwalabackend.config;

import com.gymwalabackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(OAuthLoginSuccessHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull Authentication authentication) throws IOException {

        try {
            logger.info("=== OAuth2 Login Success Handler Called ===");
            
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            logger.debug("Principal retrieved successfully");

            assert user != null;
            logger.debug("User attributes: {}", user.getAttributes());

            // Extract email - REQUIRED for JWT subject
            Object emailObj = user.getAttribute("email");
            System.out.println("User attribute value: " + user.getAttribute("profile"));
            String email = emailObj != null ? emailObj.toString() : null;
            
            if (email == null || email.isEmpty()) {
                logger.error("Email attribute is null or empty! Cannot generate JWT.");
                throw new IllegalArgumentException("Email is required for JWT generation");
            }
            
            logger.info("User email: {}", email);

            // Build claims map with all available user attributes
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", email);
            
            // Add optional attributes with null-safe handling
            Object nameObj = user.getAttribute("name");
            claims.put("name", nameObj != null ? nameObj.toString() : "");
            
            Object givenNameObj = user.getAttribute("given_name");
            claims.put("given_name", givenNameObj != null ? givenNameObj.toString() : "");
            
            Object familyNameObj = user.getAttribute("family_name");
            claims.put("family_name", familyNameObj != null ? familyNameObj.toString() : "");
            
            Object pictureObj = user.getAttribute("picture");
            claims.put("picture", pictureObj != null ? pictureObj.toString() : "");
            
            logger.debug("JWT Claims prepared: {}", claims);

            // Generate JWT token
            String token = jwtUtil.generateToken(claims);
            
            if (token == null || token.isEmpty()) {
                logger.error("JWT token generation returned null or empty");
                throw new RuntimeException("Failed to generate JWT token");
            }
            
            logger.info("JWT token generated successfully (length: {})", token.length());

            // Build redirect URL
            String redirectUrl = "http://localhost:8100/oauth-success?token=" + token;
//            String redirectUrl = "http://localhost:4200/oauth-success?token=" + token;
            logger.info("Redirecting to: {}", redirectUrl);
            
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            logger.info("=== OAuth2 Login Success Handler Completed ===");
            
        } catch (IllegalArgumentException e) {
            logger.error("Illegal argument: {}", e.getMessage(), e);
            sendErrorResponse(response, "Invalid data: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Runtime exception during token generation: {}", e.getMessage(), e);
            sendErrorResponse(response, "Token generation failed: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO exception during redirect: {}", e.getMessage(), e);
            sendErrorResponse(response, "Redirect failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected exception in OAuth2 success handler", e);
            logger.error("Exception type: {}", e.getClass().getName());
            logger.error("Exception message: {}", e.getMessage());
            sendErrorResponse(response, "Unexpected error: " + e.getMessage());
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) {
        try {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            String jsonError = String.format("{\"error\":\"%s\"}", errorMessage.replace("\"", "\\\""));
            response.getWriter().write(jsonError);
            response.getWriter().flush();
        } catch (IOException ioException) {
            logger.error("Failed to send error response", ioException);
        }
    }
}
