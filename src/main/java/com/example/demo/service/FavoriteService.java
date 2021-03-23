package com.example.demo.service;

import com.example.demo.entities.Favorite;
import com.example.demo.entities.User;
import com.example.demo.repositories.FavoriteRepo;
import com.example.demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoriteService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;


    //Hämta favoriter genom inmattad id- denna funktionen behövs för att filtrera
    //inloggad users favorites
    public Favorite getFavoriteById(long id) {
        Optional<Favorite> favorite = favoriteRepo.findById(id);
        if(favorite.isPresent()){
            return favorite.get();
        }
        return null;
    }

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

    public String deleteFavorite(long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email);

        // Hämtar id från inloggad User
        long userId = user.getUserId();

        // Hämtar id i DB genom inmatad id i metoden
        Favorite favorite = getFavoriteById(id);

        // Hämtar user_id (fk i favorite tabellen i DB) från den favorite som ska raderas
        long favoritesUserId = favorite.getUserId();

        // Bara inloggad User kan ta bort sina egna favoriter
        if(userId == favoritesUserId){
            favoriteRepo.deleteById(id);
            return "Favorit med id: " + id + " är nu borttagen.";
        }else{
            return "Kunde inte hitta favoriten.";
        }
    }


    public List<Map> getFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Favorite> favoriteList = favoriteRepo.allMyFavoritesByEmail(email);

        return filterFavorites(favoriteList);
    }
    
    public List<Map> filterFavorites(List<Favorite> favorites){

        List<Map> filteredFavoriteList = new ArrayList<>();

        for(Favorite favoriteItem : favorites){
            String url = favoriteItem.getUrl();

            if(url.contains("program")){
                Map programContent = Map.of(
                        "type", "Program",
                        "name", favoriteItem.getName(),
                        "image", favoriteItem.getImage(),
                        "url", url
                );
                filteredFavoriteList.add(programContent);

            }else{
                Map broadcastContent = Map.of(
                        "type", "Broadcast",
                        "name", favoriteItem.getName(),
                        "image", favoriteItem.getImage(),
                        "url", url
                );
                filteredFavoriteList.add(broadcastContent);

            }

        }

        return filteredFavoriteList;
    }


}