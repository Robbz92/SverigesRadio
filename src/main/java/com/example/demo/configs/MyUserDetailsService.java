package com.example.demo.configs;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
public class MyUserDetailsService implements UserDetailsService {
    /*
      Detta är vår config/Service
   */
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public BCryptPasswordEncoder getEncoder() { return encoder; }

    @Autowired
    private UserRepo userRepo;

    /*
        Detta är bara vår konstruktor.
        Denna metod är bara för att testa
        senare tar vi bort denna för att ersätta en annan bättre metod för att slippa hårdkoda.
        Detta görs då i vår kontroller..
     */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by name: " + email);
        }
        return toUserDetails(user);
    }

    public User registerUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            return userRepo.save(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private UserDetails toUserDetails(User user) {
        // If you have a User entity you have to
        // use the userdetails User for this to work
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER").build();
    }
}
