package com.example.demo.controllers;

import com.example.demo.entities.Friend;
import com.example.demo.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("auth/friends")
    public Friend addFriend(@RequestBody Friend friend){return friendService.addFriend(friend);}


}