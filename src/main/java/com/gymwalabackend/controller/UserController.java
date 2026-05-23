package com.gymwalabackend.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.gymwalabackend.entity.Member;
import com.gymwalabackend.services.MemberService;
import com.nimbusds.oauth2.sdk.Response;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @ElementCollection
    @CollectionTable(name = "user_attributes", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "value")
    @MapKeyColumn(name = "attribute_key")

    public final MemberService memberService;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    Member member;

    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/user")
    public Map<String, Object> user(Authentication authentication) {
        logger.info("User endpoint called");
        logger.debug("Authentication type: {}", authentication.getClass().getName());
        logger.debug("Principal type: {}", authentication.getPrincipal().getClass().getName());

        System.out.println("Principal type: " + authentication.getPrincipal().getClass().getName());
        Member member = new Member();
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if it's JWT authentication (from Authorization Bearer token)
            if (authentication instanceof JwtAuthenticationToken) {
                logger.info("JWT Authentication detected");
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                Jwt jwt = jwtAuth.getToken();


                response.put("given_name", jwt.getClaimAsString("given_name"));
//                attributes.put("firstName", jwt.getClaimAsString("given_name"));
                response.put("family_name", jwt.getClaimAsString("family_name"));
//                attributes.put("lastName", jwt.getClaimAsString("family_name"));
                response.put("name", jwt.getClaimAsString("name"));
//                attributes.put("fullName", jwt.getClaimAsString("name"));
                response.put("email", jwt.getSubject());
//                attributes.put("email", jwt.getSubject());
                response.put("provider_name", jwt.getClaimAsString("auth_type"));
//                attributes.put("provider", jwt.getClaimAsString("auth_type"));
                response.put("pictureUrl", jwt.getClaimAsString("picture"));
//                attributes.put("pictureUrl", jwt.getClaimAsString("picture"));

                logger.info("JWT user info returned for: {}", jwt.getSubject());
                return response;
            }

            // Check if it's OAuth2 authentication (from OAuth login)
            if (authentication.getPrincipal() instanceof OAuth2User) {
                logger.info("OAuth2 Authentication detected");
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

                Object emailObj = oauth2User.getAttribute("email");
                String email = emailObj != null ? emailObj.toString() : "";


                response.put("given_name", oauth2User.getAttribute("given_name"));
                member.setFirstName(oauth2User.getAttribute("given_name"));
                response.put("family_name", oauth2User.getAttribute("family_name"));
                member.setLastName(oauth2User.getAttribute("family_name"));
                response.put("name", oauth2User.getAttribute("name"));
                member.setFullName(oauth2User.getAttribute("name"));
                response.put("gender",oauth2User.getAttribute("gender"));
                member.setGender(oauth2User.getAttribute("gender"));
                response.put("email", email);
                member.setEmail(email);
                response.put("provider_name", oauth2User.getAttribute("auth_type"));
                member.setProvider(oauth2User.getAttribute("auth_type"));
                response.put("pictureUrl", oauth2User.getAttribute("picture"));
                member.setPictureUrl(oauth2User.getAttribute("picture"));
                System.out.println("Oauth2"+oauth2User);
                System.out.println("Oauth2Object"+member.getGender());

                memberService.saveOrUpdateMember(member);

//    /                System.out.println("result- "+result);
                logger.info("OAuth2 user info returned for: {}", email);
                return response;
            }

            // If we reach here, authentication type is not recognized
            logger.error("Unknown authentication type: {}", authentication.getClass().getName());
            response.put("error", "Unknown authentication type");
            response.put("auth_type", authentication.getClass().getSimpleName());
            return response;

        } catch (Exception e) {
            logger.error("Error in user endpoint", e);
            response.put("error", "Failed to retrieve user info: " + e.getMessage());
            return response;
        }
    }
    @GetMapping("/profile")
    public Map<String, Object> getUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        // Returns the OIDC ID Token attributes matching the requested scopes
        Map<String, Object> response = new HashMap<>();
        response=oidcUser.getAttributes();
        for (Map.Entry<String, Object> entry : response.entrySet()) {
//            logger.info("Claim: {} = {}", entry.getKey(), entry.getValue());
            System.out.println("Claim: " + entry.getKey() + " : " + entry.getValue());
        }
        return oidcUser.getClaims();
    }
//    @GetMapping("/user")
//    public ResponseEntity<?> user(Authentication authentication) {
//        logger.info("User endpoint called");
//        logger.debug("Authentication type: {}", authentication.getClass().getName());
//        logger.debug("Principal type: {}", authentication.getPrincipal().getClass().getName());
//        System.out.println("authentication Trail 1"+ authentication.getPrincipal().getClass());
//        System.out.println("authentication Trail 2"+ authentication.getPrincipal());
//        System.out.println("authentication Trail 3"+ authentication);
//
//        Member member = new Member();
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            // Check if it's OAuth2 authentication (from OAuth login)
//            if (authentication.getPrincipal() instanceof OAuth2User) {
//                logger.info("OAuth2 Authentication detected");
//                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
//
//                Object emailObj = oauth2User.getAttribute("email");
//                String email = emailObj != null ? emailObj.toString() : "";
//
//
//                member.setFirstName(oauth2User.getAttribute("given_name"));
//                member.setLastName(oauth2User.getAttribute("family_name"));
//                member.setFullName(oauth2User.getAttribute("name"));
//                member.setEmail(email);
//                member.setProvider(oauth2User.getAttribute("auth_type"));
//                member.setPictureUrl(oauth2User.getAttribute("picture"));
//
//                Member saved = memberService.saveOrUpdateMember(member);
//                logger.info("OAuth2 user info returned for: {}", email);
//                return ResponseEntity.ok(saved);
//
//            }
//
//            // If we reach here, authentication type is not recognized
//        }catch (DataIntegrityViolationException e) {
//                logger.error("Duplicate entry error in user endpoint", e);
//                Map<String, Object> catchresponse = new HashMap<>();
//                catchresponse.put("error", "Email already registered");
//                catchresponse.put("email", member.getEmail());
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(catchresponse);
//            }
//             catch (Exception e) {
//            logger.error("Error in user endpoint", e);
//            response.put("error", "Failed to retrieve user info: " + e.getMessage());
//            response.put("auth_type", authentication != null ? authentication.getClass().getSimpleName() : "N/A");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }




    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

}
