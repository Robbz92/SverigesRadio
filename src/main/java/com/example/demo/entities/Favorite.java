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

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public Favorite(){}

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
