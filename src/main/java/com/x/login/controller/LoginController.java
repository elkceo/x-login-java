package com.x.login.controller;


import com.x.login.domain.UserProfile;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String homePage(HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("loggedIn");
        model.addAttribute("loggedIn", Boolean.TRUE.equals(isLoggedIn));

        if (Boolean.TRUE.equals(isLoggedIn)) {
            UserProfile userProfile = (UserProfile) session.getAttribute("userProfile");
            model.addAttribute("userProfile", userProfile);
        }
        return "home";  // returns home.html
    }

}

