package com.example.demo.service;

import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    /*
        Detta är vår controller!
     */

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepo userRepo;

    public User register(User user){return myUserDetailsService.registerUser(user);}

    /*
        här kan vi även lägga till en whoami metod för att visa aktiv användre
     */
}
