package com.example.demo.service;

import com.example.demo.entities.Favorite;
import com.example.demo.repositories.FavoriteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepo favoriteRepo;

    public Favorite addFavorite(Favorite favorite){
        return favoriteRepo.save(favorite);
    }
}