package com.example.demo.controllers;

import com.example.demo.entities.Friend;
import com.example.demo.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("auth/friends")
    public Friend addFriend(@RequestBody Friend friend){return friendService.addFriend(friend);}

    @DeleteMapping("/auth/deleteFriend/{id}")
    public void deleteFriend(@PathVariable long id){friendService.removeFriend(id);}

    // get friends.....
}