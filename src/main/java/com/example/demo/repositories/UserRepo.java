package com.example.demo.repositories;

import com.example.demo.entities.Friend;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /*
        Detta är vårt repo interface!
     */
    User findByEmail(String email);
}