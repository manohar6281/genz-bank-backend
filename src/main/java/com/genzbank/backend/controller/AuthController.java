package com.genzbank.backend.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.genzbank.backend.dto.UserResponse;

import com.genzbank.backend.entity.User;

import com.genzbank.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController

@RequestMapping("/api/auth")

@RequiredArgsConstructor

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://genz-bank-frontend.vercel.app"
})

public class AuthController {

    private final UserService userService;

    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody User user) {

        try {

            User loggedInUser = userService.loginUser(

                    user.getEmail(),

                    user.getPassword()

            );

            UserResponse response = new UserResponse(

                    loggedInUser.getId(),

                    loggedInUser.getName(),

                    loggedInUser.getEmail(),

                    loggedInUser.getPhone(),

                    loggedInUser.getProfilePicture(),

                    loggedInUser.getCreatedAt()

            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity

                    .status(401)

                    .body(e.getMessage());

        }

    }

}