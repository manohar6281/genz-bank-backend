package com.genzbank.backend.service;

import org.springframework.stereotype.Service;

import com.genzbank.backend.entity.User;
import com.genzbank.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(User user) {

        return userRepository.save(user);

    }


    public User getUserById(Long id) {

        return userRepository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

    }


    /*
     * UPDATE PROFILE PICTURE
     */
    public User updateProfilePicture(
            Long id,
            String profilePicture
    ) {

        User user = getUserById(id);

        user.setProfilePicture(profilePicture);

        return userRepository.save(user);

    }


    public User loginUser(
            String email,
            String password
    ) {

        User user = userRepository.findByEmail(email)

                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );


        if (!user.getPassword().equals(password)) {

            throw new RuntimeException(
                    "Invalid email or password"
            );

        }


        return user;

    }

}