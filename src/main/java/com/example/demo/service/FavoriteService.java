package com.example.demo.service;

import com.example.demo.entities.Favorite;
import com.example.demo.entities.User;
import com.example.demo.repositories.FavoriteRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;

    public Favorite addFavorite(Favorite favorite){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        long userId = user.getUserId();

        if(userRepo.existsById(userId)){
            Favorite favorite1 = new Favorite(favorite.getUrl(),favorite.getImage(),favorite.getName(), userId);

            favoriteRepo.save(favorite1);

            return favorite1;
        }
        return null;
    }

    public void deleteFavorite(long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);
        long userId = user.getUserId();

        if(userRepo.existsById(userId)){
            Favorite favoriteID = new Favorite(id);

            favoriteRepo.delete(favoriteID);
        }
    }
}