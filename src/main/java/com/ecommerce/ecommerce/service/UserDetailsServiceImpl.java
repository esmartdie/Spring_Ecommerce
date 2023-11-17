package com.ecommerce.ecommerce.service;


import com.ecommerce.ecommerce.model.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("This is the username");
        Optional<User> optionalUser = userService.findByEmail(username);
        if(optionalUser.isPresent()){
            LOG.info("This is the user id: {}", optionalUser.get().getId());
            session.setAttribute("userId", optionalUser.get().getId());
            User user = optionalUser.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getName())
                    .password(user.getPassword())
                    .roles(user.getUserRol())
                    .build();
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Autowired
    private IUserService userService;
    @Autowired
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    HttpSession session;

    private final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
}
