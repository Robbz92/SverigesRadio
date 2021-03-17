package com.example.demo.controllers;

import com.example.demo.configs.GenericObject;
import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/rest/getAllChannels")
    public List<GenericObject> getAllChannels(){return userService.getAllChannels();}

    @PostMapping("/rest/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/rest/getAllFriends")
    public List<User> getAllFriends(){
        return userService.getAll();
    }


}