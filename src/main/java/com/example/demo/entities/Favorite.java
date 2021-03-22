package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="favorite_id")
    private long favoriteId;

    private String url;
    private String name;
    private String image;

    @Column(name = "user")
    private long userId;

    @ManyToOne
    @JoinColumn(name = "user", insertable = false, updatable = false)
    private User user;

    public Favorite(){}


    public Favorite(String url, String image, String name, long userId) {
        this.url = url;
        this.image = image;
        this.name = name;
        this.userId = userId;
    }

    public Favorite(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    @JsonIgnore
    public long getFavoriteId() {
        return favoriteId;
    }

    @JsonProperty
    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
    }

    @JsonIgnore
    public long getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    @JsonIgnore
    public User getUser() {return user;}

    @JsonProperty
    public void setUser(User user) {this.user = user;}

    @Override
    public String toString() {
        return "Favorite{" +
                "favoriteId=" + favoriteId +
                ", url='" + url + '\'' +
                ", user=" + user +
                '}';
    }
}