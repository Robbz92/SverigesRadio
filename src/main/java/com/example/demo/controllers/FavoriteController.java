package com.example.demo.controllers;

import com.example.demo.entities.Favorite;
import com.example.demo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    //Userstory 11
    @PostMapping("/auth/favorites")
    public Favorite addFavorite(@RequestBody Favorite favorite){return favoriteService.addFavorite(favorite);}

    //Userstory 12 -> Genom detta får vi type av favorite d v s programs eller broadcasts
    // HÄMTAR BARA FAVORITER TILL DEN INLOGGADE ANVÄNDAREN-> FUNGERAR
    @GetMapping("/auth/getMyFavoritesType")
    public List<Map> getFavorites(){
        return favoriteService.getMyFavorites();
    }

    //Har Fixat funtionen i favoriteService -> NU FUNGERAR KORREKT!
    @DeleteMapping("/auth/deleteFavorite/{id}")
    public String deleteFavorite(@PathVariable long id){return favoriteService.deleteFavorite(id);}

}