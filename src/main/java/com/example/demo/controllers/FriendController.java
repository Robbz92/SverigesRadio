package com.example.demo.controllers;

import com.example.demo.entities.Friend;
import com.example.demo.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    // Userstory 13
    @PostMapping("auth/friends")
    public Friend addFriend(@RequestBody Friend friend){return friendService.addFriend(friend);}

    // Userstory 13
    //Har Fixat funtionen i friendService -> NU FUNGERAR KORREKT!
    @DeleteMapping("/auth/deleteFriend/{id}")
    public String deleteFriend(@PathVariable long id){return friendService.removeFriend(id);}

    //Userstory 14
    // HÄMTAR BARA VÄNNAR TILL DEN INLOGGADE ANVÄNDAREN-> FUNGERAR
    @GetMapping("/auth/getAllMyFriends")
    public List<Friend> getAllFriends(){
        return friendService.getAllMyFriends();
    }
}