package com.example.user_service.user_service.controller;

import com.example.user_service.user_service.model.User;
import com.example.user_service.user_service.service.UserService;
import com.example.user_service.user_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class JwtController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
       return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/protected")
    public String protectedRoute(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Missing or invalid token";
        }

        String token = authHeader.substring(7);
        if (!JwtUtil.validateToken(token)) {
            return "Invalid or expired token";
        }
        String username = JwtUtil.getUsernameFromToken(token);
        return "Hello, " + username;

    }


}
