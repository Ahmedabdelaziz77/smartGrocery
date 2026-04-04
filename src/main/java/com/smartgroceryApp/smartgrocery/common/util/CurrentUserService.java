package com.smartgroceryApp.smartgrocery.common.util;

import com.smartgroceryApp.smartgrocery.common.exception.ResourceNotFoundException;
import com.smartgroceryApp.smartgrocery.user.entity.User;
import com.smartgroceryApp.smartgrocery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String email = getCurrentUserEmail();

        if (email == null) {
            throw new ResourceNotFoundException("authenticated user doesn't exist!");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("authenticated user not found !!"));
    }

    private static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return authentication.getName();
    }
}
