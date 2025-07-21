package com.ecommerce.eCom.util;

import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

@Autowired
    UserRepository userRepository;


    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found!!"));
        return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found!!"));
        return user.getUserId();
    }

    public User loggedInuser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found!!"));
        return user;
    }
}
