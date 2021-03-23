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

    // User story 1 Fungerar
    @GetMapping("/rest/channels")
    public List<Map> getAllChannels(){
        return userService.getAllOptions("channels", "channels");
    }

    // User story 2 Fungerar
    @GetMapping("/rest/broadcasts")
    public List<Map> getAllBroadcasts(){
        return userService.getAllOptions("scheduledepisodes/rightnow", "channels");
    }

    // User story 3 Fungerar (id = 163)
    @GetMapping("/rest/programs/channel/{id}")
    public List<Map> getProgramsByChannelId(@PathVariable int id){
        return userService.getAllOptionsById("programs/index?channelid=", "programs", id);
    }

    // User story 4 Fungerar
    @GetMapping("/rest/categories")
    public List<Map> getAllCategories(){
        return userService.getAllOptions("programcategories", "programcategories");
    }

    // User story 5 Fungerar
    @GetMapping("/rest/programs/category/{id}")
    public List<Map> getProgramsByCategoryId(@PathVariable int id){
        return userService.getAllOptionsById("programs/index?programcategoryid=", "programs", id);
    }

    // User story 6 Fungerar
    @GetMapping("/rest/programs/{input}")
    public List<Map> searchProgram(@PathVariable String input){
        return userService.searchProgram(input);
    }

    // User story 7 Fungerar
    @GetMapping("/rest/description/{id}")
    public Map getDescriptionById(@PathVariable int id){return userService.getDescriptionById(id);}

    // User story 8 Fungerar
    @GetMapping("/rest/program/broadcasts/{id}")
    public List<Map> getProgramBroadcasts(@PathVariable int id){
        return userService.getAllOptionsById("broadcasts?programid=","broadcasts", id);
    }

    // ----->

    // User story 9 Fungerar
    @PostMapping("/rest/register")
    public User register(@RequestBody User user){

        return userService.register(user);
    }

    @GetMapping("/auth/whoami")
    public User whoAmI(){
        return userService.whoAmI();
    }

    /*
    @GetMapping("/auth/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

     */

    // User story 13 Fungerar
    @PutMapping("/auth/friends")
    public User addFriend(@RequestBody User friend){
        return userService.addFriend(friend);
    }

    // User story 13 Fungerar
    @DeleteMapping("/auth/friends/{id}")
    public String deleteFriend(@PathVariable long id) {
        return userService.deleteFriendById(id);
    }

}