package com.example.demo.service;

import com.example.demo.configs.MyUserDetailsService;
import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import com.example.demo.repositories.FriendRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private FriendRepo friendRepo;

    @Autowired
    private UserRepo userRepo;

    //
    public Friend getFriendById(long id) {
        Optional<Friend> friend = friendRepo.findById(id);
        if(friend.isPresent()){
            return friend.get();
        }
        return null;
    }

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

    public String removeFriend(long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        long userId = user.getUserId();// hämtar id från inloggade user

        Friend friend = getFriendById(id);//hämtar object-friend i DB genom inmattad id i metoden
        long friendsUserId = friend.getUserId();//hämtar user-id(fk i friends tabellen i DB) från den vän som vill raderas

        if(userId == friendsUserId){ //Är den som vill raderas vän till den inloggade usern?
            friendRepo.deleteById(id);
            return "Your friend is deleted!";
        }else{
            return "You are not authorized to delete this friend!";
        }
    }

    public List<Friend> getAllMyFriends() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendRepo.allMyFriendsByEmail(email);
    }

}
