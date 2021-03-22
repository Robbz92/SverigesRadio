package com.example.demo.service;

import com.example.demo.entities.Favorite;
import com.example.demo.entities.Friend;
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
        long userId = user.getUserId();// hämtar id från inloggade user

        Favorite favorite = getFavoriteById(id);//hämtar object-friend i DB genom inmattad id i metoden
        long favoritesUserId = favorite.getUserId();//hämtar user-id(fk i friends tabellen i DB) från den vän som vill raderas

        if(userId == favoritesUserId){ //Är den som vill raderas vän till den inloggade usern?
            favoriteRepo.deleteById(id);
            return "Your favorite is deleted!"; //engelska eller svenska???
        }else{
            return "You are not authorized to delete this favorite!";
        }
    }

    //Om vi bestämmer oss för att filtrera programs och broadcasts
    public List<Map> getMyFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Favorite> favorites = favoriteRepo.allMyFavoritesByEmail(email);

        return filterFavoritesType(favorites);
    }
    public List<Map> filterFavoritesType(List<Favorite> favorites){
        List<Map> filteredFavos = new ArrayList<>();

        for(Favorite fav : favorites){
            String url = fav.getUrl();

            if(url.contains("broadcasts")){
                Map broadc = Map.of(
                        "type", "Broadcast",
                        "name", fav.getName(),
                        "image", fav.getImage(),
                        "url", url
                );
                filteredFavos.add(broadc);

            }
            if(url.contains("programs")){
                Map prog = Map.of(
                        "type", "Program",
                        "name", fav.getName(),
                        "image", fav.getImage(),
                        "url", url
                );
                filteredFavos.add(prog);

            }
        }

        return filteredFavos;
    }

    ////Om vi bestämmer oss för att hämta användares favoriter utan att filtrera type dvs programs och broadcasts
    public List<Favorite> getJustMyFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteRepo.allMyFavoritesByEmail(email);
    }
}