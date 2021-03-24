package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    /*
        Detta är vårt repo interface!
     */
    User findByEmail(String email);

    @Query(value="DELETE FROM friends WHERE friend_id= ?1 AND user = ?2",nativeQuery = true)
    void deleteFriend(long id, long userId);
}