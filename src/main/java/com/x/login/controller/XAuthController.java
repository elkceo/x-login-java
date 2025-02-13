package com.x.login.controller;


import com.x.login.domain.AccessTokenResponse;
import com.x.login.domain.UserProfile;
import com.x.login.service.XAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class XAuthController {

    private final XAuthService xAuthService;

    @GetMapping("/login")
    public RedirectView loginWithX() {
        try {
            String authUrl = xAuthService.getAuthUrl();
            log.info("Redirecting user to: {}", authUrl);
            return new RedirectView(authUrl);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return new RedirectView("/error?message=" + e.getMessage());
        }
    }

    @GetMapping("/callback")
    public RedirectView callback(
            @RequestParam("oauth_token") String oauthToken,
            @RequestParam("oauth_verifier") String oauthVerifier,
            HttpSession session
    ) {
        try {
            AccessTokenResponse tokenResponse = xAuthService.getAccessToken(oauthToken, oauthVerifier);
            log.info("Token: {}", tokenResponse.getToken());
            log.info("Secret: {}", tokenResponse.getTokenSecret());
            UserProfile userProfile = xAuthService.getUserProfile(tokenResponse);

            session.setAttribute("loggedIn", true);
            session.setAttribute("accessToken", tokenResponse);
            session.setAttribute("userProfile", userProfile);

            log.info("User successfully logged in! {}", userProfile);
            return new RedirectView("/");
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return new RedirectView("/error?message=" + e.getMessage());
        }
    }


    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        log.info("User logged out.");
        return new RedirectView("/");
    }
}
