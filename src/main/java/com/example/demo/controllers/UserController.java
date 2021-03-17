package com.example.demo.controllers;

import com.example.demo.configs.GenericObject;
import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/rest/getAllChannels")
    public List<GenericObject> getAllChannels(){return userService.getAllOptions("channels", "channels");}

    @GetMapping("/rest/getAllPrograms")
    public List<GenericObject> getAllPrograms(){return userService.getAllOptions("programs", "programs");}

    @GetMapping("/rest/getAllCategories")
    public List<GenericObject> getAllCategorie(){return userService.getAllOptions("programcategories", "programcategories");}

    @GetMapping("/rest/getAllBroadcasts")
    public List<GenericObject> getAllBroadcasts(){return userService.getAllOptions("scheduledepisodes/rightnow", "channels");}

    @GetMapping("/rest/getProgramsByChannelId/{id}")
    public List<GenericObject> getProgramsByChannelId(@PathVariable int id){return userService.getAllOptionsById("programs/index?channelid=", "programs", id);}

    @GetMapping("/rest/getProgramsByCategoryId/{id}")
    public List<GenericObject> getProgramsByCategoryId(@PathVariable int id){return userService.getAllOptionsById("programs/index?programcategoryid=", "programs", id);}

    @PostMapping("/rest/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/rest/getAllFriends")
    public List<User> getAllFriends(){
        return userService.getAll();
    }


}