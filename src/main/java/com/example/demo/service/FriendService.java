package com.example.demo.service;

import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import com.example.demo.repositories.FriendRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

    @Autowired
    private UserRepo userRepo;

    public Friend addFriend(Friend friend) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        long userId = user.getUserId();

        if(userRepo.existsById(userId)){
            Friend friend1 = new Friend(friend.getEmail(),friend.getFirstName(),userId);

            friendRepo.save(friend1);

            return friend1;
        }
        return null;
    }
}
