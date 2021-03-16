package com.example.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepo extends JpaRepository<Friend, Long> {

    List<Channel> getAllChannels();
}
