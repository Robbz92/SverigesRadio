package com.example.demo.controllers;

import com.example.demo.entities.Favorite;
import com.example.demo.entities.Friend;
import com.example.demo.service.FavoriteService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("auth/favorite")
    public Favorite addFavorite(@RequestBody Favorite favorite){return favoriteService.addFavorite(favorite);}
}
