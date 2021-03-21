package com.example.demo.repositories;

import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepo extends JpaRepository<Friend, Long> {

    @Query(value = "SELECT * FROM friends INNER JOIN user ON friends.user = user.user_id WHERE user.email = ?1", nativeQuery = true)
    List<Friend> allMyFriendsByEmail(String email);
}
