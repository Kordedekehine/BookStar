package com.artistbooking.BookArtist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginConroller {

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/";
        } else {

//        // get current user from Security Context
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (!username.equals("anonymousUser")) {
//            return "redirect:/"; // if user already logged in redirect back to root context
//        } else {
            return "login";
        }
    }
}
