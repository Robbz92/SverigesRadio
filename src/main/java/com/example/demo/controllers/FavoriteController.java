package com.example.demo.controllers;

import com.example.demo.entities.Favorite;
import com.example.demo.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // User story 11 Fungerar
    @PostMapping("/auth/favorites")
    public Favorite addFavorite(@RequestBody Favorite favorite){return favoriteService.addFavorite(favorite);}

    //User story 12 -> Genom detta får vi type av favorite d v s programs eller broadcasts
    // HÄMTAR BARA FAVORITER TILL DEN INLOGGADE ANVÄNDAREN-> FUNGERAR
    @GetMapping("/auth/favorites")
    public List<Map> getFavorites(){
        return favoriteService.getFavorites();
    }

    //Har Fixat funktionen i favoriteService -> NU FUNGERAR KORREKT!
    @DeleteMapping("/auth/favorites/{id}")
    public String deleteFavorite(@PathVariable long id){return favoriteService.deleteFavorite(id);}

}