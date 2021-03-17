package com.example.demo.service;

import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.repositories.FriendRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class FriendService {

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepo friendRepo;


}
