package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/rest/getAllChannels")
    public List<Friends> getAllChannels(){
        return friendService.getAllChannels();
    }
}
