package com.genzbank.backend.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genzbank.backend.dto.UserResponse;
import com.genzbank.backend.entity.User;
import com.genzbank.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;


    /*
     * CREATE USER
     */
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user
    ) {

        User createdUser =
                userService.createUser(user);

        return ResponseEntity.ok(createdUser);

    }


    /*
     * UPDATE PROFILE PICTURE
     */
    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @PathVariable Long id,
            @RequestBody ProfilePictureRequest request
    ) {

        try {

            String picture =
                    request.getProfilePicture();


            /*
             * Prevent very large images.
             *
             * The frontend compresses the image
             * before sending it.
             */
            if (
                    picture != null &&
                    picture.length() > 700_000
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Profile picture is too large. Please choose a smaller image."
                        );

            }


            User updatedUser =
                    userService.updateProfilePicture(
                            id,
                            picture
                    );


            UserResponse response =
                    new UserResponse(

                            updatedUser.getId(),

                            updatedUser.getName(),

                            updatedUser.getEmail(),

                            updatedUser.getPhone(),

                            updatedUser.getProfilePicture(),

                            updatedUser.getCreatedAt()

                    );


            return ResponseEntity.ok(response);

        }

        catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        }

    }


    /*
     * REQUEST BODY
     */
    public static class ProfilePictureRequest {

        private String profilePicture;


        public String getProfilePicture() {

            return profilePicture;

        }


        public void setProfilePicture(
                String profilePicture
        ) {

            this.profilePicture =
                    profilePicture;

        }

    }

}