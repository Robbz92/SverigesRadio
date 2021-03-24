package com.example.demo.repositories;

import com.example.demo.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface FavoriteRepo extends JpaRepository<Favorite, Long> {

    @Query(value = "SELECT * FROM favorites LEFT JOIN user ON favorites.user = user.user_id WHERE user.email = ?1", nativeQuery = true)
    List<Favorite> allMyFavoritesByEmail(String email);
}