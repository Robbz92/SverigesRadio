package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepo;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    // Userstory 1 Fungerar
    @GetMapping("/rest/channels")
    public List<Map> getAllChannels(){return userService.getAllOptions("channels", "channels");}

    // Userstory 2 Fungerar // fixa date formatet
    @GetMapping("/rest/broadcasts")
    public List<Map> getAllBroadcasts(){return userService.getAllOptions("scheduledepisodes/rightnow", "channels");}

    // Userstory 3 Fungerar 163
    @GetMapping("/rest/programs/channel/{id}")
    public List<Map> getProgramsByChannelId(@PathVariable int id){return userService.getAllOptionsById("programs/index?channelid=", "programs", id);}

    // Userstory 4 Fungerar
    @GetMapping("/rest/categories")
    public List<Map> getAllCategories(){return userService.getAllOptions("programcategories", "programcategories");}

    // Userstory 5 Fungerar
    @GetMapping("/rest/programs/category/{id}")
    public List<Map> getProgramsByCategoryId(@PathVariable int id){return userService.getAllOptionsById("programs/index?programcategoryid=", "programs", id);}

    // Userstory 6 Fungerar
    @GetMapping("/rest/programs/{input}")
    public List<Map> searchProgram(@PathVariable String input){
        return userService.searchProgram(input);
    }

    // Userstory 7 // Fungerar
    @GetMapping("/rest/description/{id}")
    public Map getDescriptionById(@PathVariable int id){return userService.getDescriptionById(id);}

    // Userstory 8
    @GetMapping("/rest/program/broadcasts/{id}")
    public List<Map> getProgramBroadcasts(@PathVariable int id){return userService.getAllOptionsById("broadcasts?programid=","broadcasts", id);}

    // ----->

    // Userstory 9
    @PostMapping("/rest/register")
    public User register(@RequestBody User user){

        return userService.register(user);
    }

    @GetMapping("/auth/whoami")
    public User whoAmI(){
        return userService.whoAmI();
    }

    @GetMapping("/auth/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/auth/friends")
    public User addFriend(@RequestBody User friend){
        return userService.addFriend(friend);
    }

    @DeleteMapping("/auth/friends/{id}")
    public String deleteFriend(@PathVariable long id) {
        return userService.deleteFriendById(id);
    }

}