package com.example.demo.service;


import com.example.demo.repositories.FriendRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

    public List<Fiend> getAllFriends(){
        return friendRepo.findAll();
    }

}
