package com.bootcamp.project.eCommerce.utils;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityUtils {

    public static User getLoggedInUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof User) {
                return ((User) authentication.getPrincipal());
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            return null;
        }
    }

    public static String getLoggedInUsername() {
        User user = getLoggedInUser();
        return Objects.isNull(user) ? null : user.getUsername();
    }

}
